package hcmut.cse.travelsocialnetwork.application.user;

import hcmut.cse.travelsocialnetwork.command.CommandLogin;
import hcmut.cse.travelsocialnetwork.command.CommandRegister;
import hcmut.cse.travelsocialnetwork.model.LoginToken;
import java.util.Optional;

public interface IUserApplication {
    Boolean register(CommandRegister commandRegister) throws Exception;
    Optional<LoginToken> login(CommandLogin commandLogin) throws Exception;
}
