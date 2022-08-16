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

    public User checkUserExist(String email) {
        var queryEmail = new Document("email", email);
        return userRepository.get(queryEmail).orElse(null);
    }
}
