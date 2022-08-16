package hcmut.cse.travelsocialnetwork.application.user;

import hcmut.cse.travelsocialnetwork.command.CommandLogin;
import hcmut.cse.travelsocialnetwork.model.LoginToken;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserApplication implements IUserApplication {


    @Override
    public Optional<LoginToken> login(CommandLogin commandLogin) throws Exception {
        if (StringUtils.equals(commandLogin.getKind(), Constant.AUTHENTICATION_KIND.INTERNAL)) {

        }
        return Optional.empty();
    }
}
