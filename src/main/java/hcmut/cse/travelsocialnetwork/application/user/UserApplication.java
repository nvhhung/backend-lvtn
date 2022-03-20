package hcmut.cse.travelsocialnetwork.application;

import hcmut.cse.travelsocialnetwork.model.User;
import hcmut.cse.travelsocialnetwork.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserApplication implements IUserApplication {

    @Autowired
    UserRepository userRepository;

    public Optional<User> addUser(String name) {
        var userNewBuild = User.builder()
                .name(name)
                .build();
        var userAddSuccess = userRepository.insert(userNewBuild);
        return Optional.of(userAddSuccess);
    }

    public Optional<User> findUser(String _id) {
        return userRepository.findById(_id);
    }
}
