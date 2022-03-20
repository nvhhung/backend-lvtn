package hcmut.cse.travelsocialnetwork.application;

import hcmut.cse.travelsocialnetwork.model.User;

import java.util.Optional;

public interface IUserApplication {
    Optional<User> addUser(String name);
    Optional<User> findUser(String _id);
}
