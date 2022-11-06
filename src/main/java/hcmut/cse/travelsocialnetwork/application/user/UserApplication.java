package hcmut.cse.travelsocialnetwork.application.user;

import hcmut.cse.travelsocialnetwork.application.search.SearchApplication;
import hcmut.cse.travelsocialnetwork.command.user.CommandLogin;
import hcmut.cse.travelsocialnetwork.command.user.CommandPassword;
import hcmut.cse.travelsocialnetwork.command.user.CommandRegister;
import hcmut.cse.travelsocialnetwork.command.user.CommandUser;
import hcmut.cse.travelsocialnetwork.model.LoginToken;
import hcmut.cse.travelsocialnetwork.model.User;
import hcmut.cse.travelsocialnetwork.repository.user.IUserRepository;
import hcmut.cse.travelsocialnetwork.service.elasticsearch.ParamElasticsearchObj;
import hcmut.cse.travelsocialnetwork.service.jwt.JWTAuth;
import hcmut.cse.travelsocialnetwork.service.jwt.JWTTokenData;
import hcmut.cse.travelsocialnetwork.service.redis.UserRedis;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.CustomException;
import hcmut.cse.travelsocialnetwork.utils.StringUtils;
import hcmut.cse.travelsocialnetwork.utils.crypto.SHA512;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class UserApplication implements IUserApplication{
    private static final Logger log = LogManager.getLogger(UserApplication.class);
    private final HelperUser helperUser;
    private final IUserRepository userRepository;
    private final JWTAuth jwtAuth;
    private final UserRedis userRedis;
    private final SearchApplication searchApplication;

    public UserApplication(HelperUser helperUser,
                           IUserRepository userRepository,
                           JWTAuth jwtAuth,
                           UserRedis userRedis,
                           SearchApplication searchApplication) {
        this.helperUser = helperUser;
        this.userRepository = userRepository;
        this.jwtAuth = jwtAuth;
        this.userRedis = userRedis;
        this.searchApplication = searchApplication;
    }

    @Override
    public Boolean register(CommandRegister commandRegister) throws Exception {
        var userTemp = helperUser.checkUserRegister(commandRegister.getUsername());
        if (userTemp != null) {
            throw new CustomException(Constant.ERROR_MSG.USER_REGISTER);
        }

        var userRegister = User.builder()
                .username(commandRegister.getUsername())
                .password(SHA512.valueOf(commandRegister.getPassword()))
                .fullName(commandRegister.getFullName())
                .phone(commandRegister.getPhone())
                .avatar(Optional.ofNullable(commandRegister.getAvatar()).orElse(""))
                .status(Constant.STATUS_USER.ACTIVE)
                .level(1)
                .isAdmin(false)
                .experiencePoint(0)
                .build();
        var userAdd = userRepository.add(userRegister);
        if (userAdd.isEmpty()) {
            throw new CustomException(Constant.ERROR_MSG.USER_REGISTER_FAIL);
        }
        userRedis.updateUser(userAdd.get().get_id().toString(), userAdd.get());
        return true;
    }

    @Override
    public Optional<LoginToken> login(CommandLogin commandLogin) throws Exception {
        // login by registered account
        var userTemp = helperUser.checkUserRegister(commandLogin.getUsername());
        if (userTemp == null) {
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUND_USER);
        }
        if (userTemp.getStatus().equals(Constant.STATUS_USER.BLOCKED)) {
            throw new CustomException(Constant.ERROR_MSG.USER_BLOCKED);
        }
        if (!SHA512.valueOf(commandLogin.getPassword()).equals(userTemp.getPassword())) {
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUND_USER);
        }
        log.info("user {} login successful", userTemp.get_id());
        return jwtAuth.createLoginToken(JWTTokenData.builder()
                .userId(userTemp.get_id().toHexString())
                .build());
    }

    @Override
    public Optional<LoginToken> resetPassword(CommandPassword commandPassword) throws Exception {
        var user = helperUser.checkUserRegister(commandPassword.getUsername());
        if (user == null) {
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUND_USER);
        }

        if (!user.getPassword().equals(commandPassword.getOldPassword())) {
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUND_USER);
        }
        user.setPassword(commandPassword.getNewPassword());
        var userUpdated  = userRepository.update(user.get_id().toString(), user);
        if (userUpdated.isEmpty()) {
            throw new CustomException(Constant.ERROR_MSG.UPDATE_USER_FAIL);
        }
        return jwtAuth.createLoginToken(JWTTokenData.builder().userId(user.get_id().toHexString()).isAdmin(false).build());
    }

    @Override
    public Optional<LoginToken> refreshToken(String accessToken, String refreshToken) throws Exception {
        return jwtAuth.refreshToken(accessToken, refreshToken);
    }

    @Override
    public Optional<User> getInfoUser(String userId) throws Exception {
        if (StringUtils.isNullOrEmpty(userId)) {
            log.info("request getInfoUser no have userId");
            throw new CustomException(Constant.ERROR_MSG.PARAM_NOT_VALID);
        }
        var user = userRedis.getUser(userId);
        return Optional.of(user);
    }

    @Override
    public Optional<User> updateInfoUser(CommandUser commandUser) throws Exception {
        var user = userRedis.getUser(commandUser.getUserId());
        if (user == null) {
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUND_USER);
        }
        Optional.ofNullable(commandUser.getFullName()).ifPresent(user::setFullName);
        Optional.ofNullable(commandUser.getPhone()).ifPresent(user::setPhone);
        Optional.ofNullable(commandUser.getEmail()).ifPresent(user::setEmail);
        Optional.ofNullable(commandUser.getBirthday()).ifPresent(user::setBirthday);
        Optional.ofNullable(commandUser.getAvatar()).ifPresent(user::setAvatar);
        Optional.ofNullable(commandUser.getCover()).ifPresent(user::setCover);
        Optional.ofNullable(commandUser.getAddress()).ifPresent(user::setAddress);
        Optional.ofNullable(commandUser.getStatus()).ifPresent(user::setStatus);

        var userUpdate = userRepository.update(commandUser.getUserId(), user);
        if (userUpdate.isEmpty()) {
            throw new CustomException(Constant.ERROR_MSG.UPDATE_USER_FAIL);
        }
        userRedis.updateUser(commandUser.getUserId(), userUpdate.get());
        return userUpdate;
    }

    @Override
    public Optional<List<User>> searchUser(CommandUser commandUser) throws Exception {
        var userListResult = new ArrayList<User>();
        var queryModel = new HashMap<String, Object>();

        Optional.ofNullable(commandUser.getKeyword()).ifPresent(keyword -> queryModel.put("keyword", commandUser.getKeyword()));
        var params = ParamElasticsearchObj.builder()
                .templateCfgKey(Constant.GLOBAL_CONFIG.QUERY_ES_USER)
                .clazz(User.class)
                .index("user")
                .from((commandUser.getPage() - 1) * commandUser.getSize())
                .size(commandUser.getSize())
                .minScore(0)
                .queryModel(queryModel)
                .build();
        var hitUserList = searchApplication.searchES(params);
        if (hitUserList == null) {
            log.warn("user from elasticsearch null");
            throw new CustomException("user from elasticsearch null");
        }

        hitUserList.forEach(hitUser -> userListResult.add(userRedis.getUser(hitUser.id())));
        return Optional.of(userListResult);
    }
}
