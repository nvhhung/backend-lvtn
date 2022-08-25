package hcmut.cse.travelsocialnetwork.application.user;

import hcmut.cse.travelsocialnetwork.command.CommandLogin;
import hcmut.cse.travelsocialnetwork.command.CommandPassword;
import hcmut.cse.travelsocialnetwork.command.CommandRegister;
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
public class UserApplication implements IUserApplication {
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
        var userTemp = helperUser.checkUserRegister(commandRegister.getUserName());
        if (userTemp != null) {
            throw new CustomException(Constant.ERROR_MSG.USER_REGISTER);
        }

        var userRegister = User.builder()
                .userName(commandRegister.getUserName())
                .password(SHA512.valueOf(commandRegister.getPassword()))
                .phone(commandRegister.getPhone())
                .avatar(Optional.ofNullable(commandRegister.getAvatar()).orElse(""))
                .status(Constant.STATUS_USER.ACTIVE)
                .level(1)
                .experiencePoint(0L)
                .build();
        var userAdd = userRepository.add(userRegister);
        redis.updateUserRedis(userAdd.get().getId().toHexString(), userAdd.get());
        return userAdd.isPresent();
    }

    @Override
    public Optional<LoginToken> login(CommandLogin commandLogin) throws Exception {
        // login by registered account
        if (StringUtils.equals(commandLogin.getKind(), Constant.AUTHENTICATION_KIND.INTERNAL)) {
            var userTemp = helperUser.checkUserRegister(commandLogin.getUserName());
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
        // login by google, facebook
        return Optional.of(null);
    }

    @Override
    public Optional<LoginToken> resetPassword(CommandPassword commandPassword) throws Exception {
        var user = helperUser.checkUserRegister(commandPassword.getUserName());
        if (user == null) {
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUNT_USER);
        }

        if (!user.getPassword().equals(commandPassword.getOldPassword())) {
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUNT_USER);
        }
        user.setPassword(commandPassword.getNewPassword());
        var userUpdated  = userRepository.update(user.getId().toHexString(), user);
//        redis.set
        return jwtAuth.createLoginToken(JWTTokenData.builder()
                .userId(user.getId().toHexString())
                .build());
    }
}
