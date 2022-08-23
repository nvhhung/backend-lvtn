package hcmut.cse.travelsocialnetwork.repository.user;

import hcmut.cse.travelsocialnetwork.factory.configuration.ENVConfig;
import hcmut.cse.travelsocialnetwork.factory.repo.GenericMongoRepository;
import hcmut.cse.travelsocialnetwork.model.User;
import hcmut.cse.travelsocialnetwork.service.mongodb.MongoDBClient;
import hcmut.cse.travelsocialnetwork.service.mongodb.MongoDBClientImpl;
import hcmut.cse.travelsocialnetwork.service.mongodb.MongoDBConfig;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * @author : hung.nguyen23
 * @since : 8/1/22 Monday
 **/
@Component
public class UserRepositoryImpl extends GenericMongoRepository<User> implements IUserRepository{

    private final MongoDBClient<User> mongoDBClient;
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    public UserRepositoryImpl(ENVConfig config) {
        mongoDBClient = new MongoDBClientImpl<>(MongoDBConfig.getInstance(MongoDBConfig
                .MongoDBConfigBuilder
                .config()
                .withConnectionURL(config.getStringProperty(Constant.KEY_CONFIG.DB))
                .withDatabaseName(Constant.DB_NAME.TRAVEL_SOCIAL_NETWORK).build()),
                config.getStringProperty(Constant.KEY_CONFIG.DB),
                Constant.DB_NAME.TRAVEL_SOCIAL_NETWORK,
                Constant.COLLECTION_NAME.USER,
                User.class);
    }

    @Override
    public MongoDBClient<User> getMongoDBOperator() {
        return mongoDBClient;
    }
}
