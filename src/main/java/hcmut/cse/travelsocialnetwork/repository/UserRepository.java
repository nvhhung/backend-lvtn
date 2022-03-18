package hcmut.cse.travelsocialnetwork.repository;

import hcmut.cse.travelsocialnetwork.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {
}
