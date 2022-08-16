package hcmut.cse.travelsocialnetwork.application.user;

import hcmut.cse.travelsocialnetwork.command.CommandLogin;
import hcmut.cse.travelsocialnetwork.command.CommandRegister;
import hcmut.cse.travelsocialnetwork.model.LoginToken;
import hcmut.cse.travelsocialnetwork.model.User;
import hcmut.cse.travelsocialnetwork.repository.user.IUserRepository;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.CustomException;
import hcmut.cse.travelsocialnetwork.utils.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Log4j2
@Component
public class UserApplication implements IUserApplication {

    @Autowired
    HelperUser helperUser;
    @Autowired
    IUserRepository userRepository;

    @Override
    public Boolean register(CommandRegister commandRegister) throws CustomException {
        var userTemp = helperUser.checkUserExist(commandRegister.getEmail());
        if (userTemp != null) {
            throw new CustomException(Constant.ERROR_MSG.USER_REGISTER);
        }

        var userRegister = User.builder()
                .name(commandRegister.getName())
                .phone(commandRegister.getPhone())
                .level(1)
                .experiencePoint(0L)
                .build();
        return null;
    }

    @Override
    public Optional<LoginToken> login(CommandLogin commandLogin) throws CustomException {
        if (StringUtils.equals(commandLogin.getKind(), Constant.AUTHENTICATION_KIND.INTERNAL)) {
            var userTemp = helperUser.checkUserExist(commandLogin.getEmail());
            if (userTemp == null) {
                throw new CustomException(Constant.ERROR_MSG.NOT_FOUNT_USER);
            }


        }
        return Optional.empty();
    }
}
