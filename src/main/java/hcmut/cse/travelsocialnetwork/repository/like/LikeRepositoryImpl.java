package hcmut.cse.travelsocialnetwork.repository.like;

import hcmut.cse.travelsocialnetwork.factory.configuration.ENVConfig;
import hcmut.cse.travelsocialnetwork.factory.repo.GenericMongoRepository;
import hcmut.cse.travelsocialnetwork.model.Like;
import hcmut.cse.travelsocialnetwork.model.Rate;
import hcmut.cse.travelsocialnetwork.repository.rate.IRateRepository;
import hcmut.cse.travelsocialnetwork.repository.rate.RateRepositoryImpl;
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
 * @since : 9/20/22 Tuesday
 **/
@Component
public class LikeRepositoryImpl extends GenericMongoRepository<Like> implements ILikeRepository {
    private final MongoDBClient<Like> mongoDBClient;
    private static final Logger log = LogManager.getLogger(LikeRepositoryImpl.class);

    @Autowired
    public LikeRepositoryImpl(ENVConfig config) {
        mongoDBClient = new MongoDBClientImpl<>(MongoDBConfig.getInstance(MongoDBConfig
                .MongoDBConfigBuilder
                .config()
                .withConnectionURL(config.getStringProperty(Constant.KEY_CONFIG.DB))
                .withDatabaseName(Constant.DB_NAME.TRAVEL_SOCIAL_NETWORK).build()),
                config.getStringProperty(Constant.KEY_CONFIG.DB),
                Constant.DB_NAME.TRAVEL_SOCIAL_NETWORK,
                Constant.COLLECTION_NAME.LIKE,
                Like.class);
    }

    @Override
    public MongoDBClient<Like> getMongoDBOperator() {
        return mongoDBClient;
    }
}
