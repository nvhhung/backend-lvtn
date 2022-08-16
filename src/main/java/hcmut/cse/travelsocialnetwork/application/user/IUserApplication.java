package hcmut.cse.travelsocialnetwork.application.user;

import hcmut.cse.travelsocialnetwork.command.CommandLogin;
import hcmut.cse.travelsocialnetwork.model.LoginToken;
import hcmut.cse.travelsocialnetwork.model.User;

import java.util.Optional;

public interface IUserApplication {
//    Optional<User> addUser(String name);
    Optional<LoginToken> login(CommandLogin commandLogin) throws Exception;
}
