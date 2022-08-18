package hcmut.cse.travelsocialnetwork.application.user;

import hcmut.cse.travelsocialnetwork.command.CommandLogin;
import hcmut.cse.travelsocialnetwork.command.CommandRegister;
import hcmut.cse.travelsocialnetwork.model.LoginToken;
import hcmut.cse.travelsocialnetwork.model.User;
import hcmut.cse.travelsocialnetwork.repository.user.IUserRepository;
import hcmut.cse.travelsocialnetwork.service.jwt.JWTAuth;
import hcmut.cse.travelsocialnetwork.service.jwt.JWTTokenData;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.CustomException;
import hcmut.cse.travelsocialnetwork.utils.StringUtils;
import hcmut.cse.travelsocialnetwork.utils.crypto.SHA512;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Log4j2
@Component
public class UserApplication implements IUserApplication {

    @Autowired
    HelperUser helperUser;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    private JWTAuth jwtAuth;

    @Override
    public Boolean register(CommandRegister commandRegister) throws Exception {
        var userTemp = helperUser.checkUserExist(commandRegister.getUserName());
        if (userTemp != null) {
            throw new CustomException(Constant.ERROR_MSG.USER_REGISTER);
        }

        var userRegister = User.builder()
                .userName(commandRegister.getName())
                .password(SHA512.valueOf(commandRegister.getPassword()))
                .phone(commandRegister.getPhone())
                .status(Constant.STATUS_USER.INACTIVE)
                .level(1)
                .experiencePoint(0L)
                .build();
        var userAdd = userRepository.add(userRegister);
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
            var loginToken = jwtAuth.createLoginToken(JWTTokenData.builder()
                    .userId(userTemp.getId().toHexString())
                    .build());

        }
        return Optional.empty();
    }
}
