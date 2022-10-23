package hcmut.cse.travelsocialnetwork.application.user;

import hcmut.cse.travelsocialnetwork.command.user.CommandLogin;
import hcmut.cse.travelsocialnetwork.command.user.CommandPassword;
import hcmut.cse.travelsocialnetwork.command.user.CommandRegister;
import hcmut.cse.travelsocialnetwork.command.user.CommandUser;
import hcmut.cse.travelsocialnetwork.model.LoginToken;
import hcmut.cse.travelsocialnetwork.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserApplication {
    Boolean register(CommandRegister commandRegister) throws Exception;
    Optional<LoginToken> login(CommandLogin commandLogin) throws Exception;
    Optional<LoginToken> resetPassword(CommandPassword commandPassword) throws Exception;
    Optional<LoginToken> refreshToken(String accessToken, String refreshToken) throws Exception;
    Optional<User> getInfoUser(String userId) throws Exception;
    Optional<User> updateInfoUser(CommandUser commandUser) throws Exception;
    Optional<List<User>> searchUser(CommandUser commandUser) throws Exception;
}
