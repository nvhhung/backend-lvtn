package hcmut.cse.travelsocialnetwork.application;

import hcmut.cse.travelsocialnetwork.model.User;
import hcmut.cse.travelsocialnetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserApplication {

    @Autowired
    UserRepository userRepository;

    public User addUser(User user) {
        return userRepository.insert(user);
    }
}
