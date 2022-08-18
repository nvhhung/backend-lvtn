package hcmut.cse.travelsocialnetwork.application.user;

import hcmut.cse.travelsocialnetwork.model.User;
import hcmut.cse.travelsocialnetwork.repository.user.IUserRepository;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : hung.nguyen23
 * @since : 8/17/22 Wednesday
 **/
@Component
public class HelperUser {
    @Autowired
    IUserRepository userRepository;

    public User checkUserRegister(String userName) {
        var queryUserName = new Document("userName", userName);
        return userRepository.get(queryUserName).orElse(null);
    }

    public User checkUserExist(String userId) {
        var queryUserId = new Document("id", userId);
        return userRepository.get(queryUserId).orElse(null);
    }
}
