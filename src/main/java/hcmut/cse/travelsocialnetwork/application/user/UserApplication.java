package hcmut.cse.travelsocialnetwork.application.user;

import hcmut.cse.travelsocialnetwork.command.user.CommandLogin;
import hcmut.cse.travelsocialnetwork.command.user.CommandPassword;
import hcmut.cse.travelsocialnetwork.command.user.CommandRegister;
import hcmut.cse.travelsocialnetwork.model.LoginToken;
import hcmut.cse.travelsocialnetwork.model.User;
import hcmut.cse.travelsocialnetwork.repository.user.IUserRepository;
import hcmut.cse.travelsocialnetwork.service.jwt.JWTAuth;
import hcmut.cse.travelsocialnetwork.service.jwt.JWTTokenData;
import hcmut.cse.travelsocialnetwork.service.redis.UserRedis;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.CustomException;
import hcmut.cse.travelsocialnetwork.utils.StringUtils;
import hcmut.cse.travelsocialnetwork.utils.crypto.SHA512;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserApplication implements IUserApplication{
    private static final Logger log = LogManager.getLogger(UserApplication.class);
    @Autowired
    HelperUser helperUser;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    private JWTAuth jwtAuth;
    @Autowired
    private UserRedis redis;

    @Override
    public Boolean register(CommandRegister commandRegister) throws Exception {
        var userTemp = helperUser.checkUserRegister(commandRegister.getUsername());
        if (userTemp != null) {
            throw new CustomException(Constant.ERROR_MSG.USER_REGISTER);
        }

    var userRegister =
        User.builder()
            .username(commandRegister.getUsername())
            .password(SHA512.valueOf(commandRegister.getPassword()))
            .phone(commandRegister.getPhone())
            .avatar(Optional.ofNullable(commandRegister.getAvatar()).orElse(""))
            .status(Constant.STATUS_USER.ACTIVE)
            .level(1)
            .isAdmin(false)
            .experiencePoint(0L)
            .build();
        var userAdd = userRepository.add(userRegister);
        if (userAdd.isEmpty()) {
            throw new CustomException(Constant.ERROR_MSG.USER_REGISTER_FAIL);
        }
        redis.updateUser(userAdd.get().getId().toString(), userAdd.get());
        return true;
    }

    @Override
    public Optional<LoginToken> login(CommandLogin commandLogin) throws Exception {
        // login by registered account
        if (StringUtils.equals(commandLogin.getKind(), Constant.AUTHENTICATION_KIND.INTERNAL)) {
            var userTemp = helperUser.checkUserRegister(commandLogin.getUsername());
            if (userTemp == null) {
                throw new CustomException(Constant.ERROR_MSG.NOT_FOUNT_USER);
            }

            if (userTemp.getStatus().equals(Constant.STATUS_USER.BLOCKED)) {
                throw new CustomException(Constant.ERROR_MSG.USER_BLOCKED);
            }

            if (!SHA512.valueOf(commandLogin.getPassword()).equals(userTemp.getPassword())) {
                throw new CustomException(Constant.ERROR_MSG.NOT_FOUNT_USER);
            }
            return jwtAuth.createLoginToken(JWTTokenData.builder()
                    .userId(userTemp.getId().toHexString())
                    .build());
        }
        return null;
    }

    @Override
    public Optional<LoginToken> resetPassword(CommandPassword commandPassword) throws Exception {
        var user = helperUser.checkUserRegister(commandPassword.getUsername());
        if (user == null) {
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUNT_USER);
        }

        if (!user.getPassword().equals(commandPassword.getOldPassword())) {
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUNT_USER);
        }
        user.setPassword(commandPassword.getNewPassword());
        var userUpdated  = userRepository.update(user.getId().toString(), user);
//        redis.set
        return jwtAuth.createLoginToken(JWTTokenData.builder()
                .userId(user.getId().toHexString())
                .isAdmin(false)
                .build());
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
        var user = redis.getUser(userId);
        return Optional.of(user);
    }

}
