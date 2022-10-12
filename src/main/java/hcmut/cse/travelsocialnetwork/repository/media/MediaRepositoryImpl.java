package hcmut.cse.travelsocialnetwork.repository.media;

import hcmut.cse.travelsocialnetwork.factory.configuration.ENVConfig;
import hcmut.cse.travelsocialnetwork.factory.repo.GenericMongoRepository;
import hcmut.cse.travelsocialnetwork.model.Media;
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
 * @since : 10/12/22 Wednesday
 **/
@Component
public class MediaRepositoryImpl extends GenericMongoRepository<Media> implements IMediaRepository {
    private final MongoDBClient<Media> mongoDBClient;
    private static final Logger log = LogManager.getLogger(MediaRepositoryImpl.class);

    @Autowired
    public MediaRepositoryImpl(ENVConfig config) {
        mongoDBClient = new MongoDBClientImpl<>(MongoDBConfig.getInstance(MongoDBConfig
                .MongoDBConfigBuilder
                .config()
                .withConnectionURL(config.getStringProperty(Constant.KEY_CONFIG.DB))
                .withDatabaseName(Constant.DB_NAME.TRAVEL_SOCIAL_NETWORK).build()),
                config.getStringProperty(Constant.KEY_CONFIG.DB),
                Constant.DB_NAME.TRAVEL_SOCIAL_NETWORK,
                Constant.COLLECTION_NAME.MEDIA,
                Media.class);
    }

    @Override
    public MongoDBClient<Media> getMongoDBOperator() {
        return mongoDBClient;
    }
}
