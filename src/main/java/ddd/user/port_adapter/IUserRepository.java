package ddd.user.port_adapter;

import ddd.user.User;
import factory.repo.GenericRepository;
import lombok.NonNull;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IUserRepository extends GenericRepository<User> {
    Optional<User> add(User user);
}
