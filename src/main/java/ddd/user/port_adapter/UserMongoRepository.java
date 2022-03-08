package ddd.user.port_adapter;

import configuration.ENVConfig;
import ddd.user.User;
import factory.repo.GenericMongoRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.CollectionEnum;
import utils.DbNameEnum;
import utils.mongodb.KijiKokoroMongoDB;
import utils.mongodb.KijoKokoroMongoDBOperator;
import utils.mongodb.MongoDBOperator;

import java.util.Optional;

@Component
public class UserMongoRepository extends GenericMongoRepository<User> implements IUserRepository{
    private final MongoDBOperator<User> mongoDBOperator;
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    public UserMongoRepository(ENVConfig applicationConfig) {
        mongoDBOperator = new KijoKokoroMongoDBOperator<>(
                KijiKokoroMongoDB.getInstance(KijiKokoroMongoDB.MongoDBConfigBuilder.config()
                        .withConnectionURL(applicationConfig.getStringProperty(DbNameEnum.KEY))
                        .withDatabaseName(DbNameEnum.USER).build()),
                applicationConfig.getStringProperty(DbNameEnum.KEY),
                DbNameEnum.USER, CollectionEnum.USER, User.class
        );
    }

    @Override
    public Optional<User> add(User user) {
        try {
//            user.setCreated_date(System.currentTimeMillis());
//            user.setLast_updated_date(System.currentTimeMillis());
            return Optional.of(mongoDBOperator.insert(user));
        } catch (Throwable throwable) {
            LOGGER.error(throwable);
            return Optional.empty();
        }
    }
}
