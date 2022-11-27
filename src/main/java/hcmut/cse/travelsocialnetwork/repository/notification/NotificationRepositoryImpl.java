package hcmut.cse.travelsocialnetwork.repository.notification;

import hcmut.cse.travelsocialnetwork.factory.configuration.ENVConfig;
import hcmut.cse.travelsocialnetwork.factory.repo.GenericMongoRepository;
import hcmut.cse.travelsocialnetwork.model.GlobalConfig;
import hcmut.cse.travelsocialnetwork.model.Notification;
import hcmut.cse.travelsocialnetwork.repository.globalconfig.GlobalConfigRepositoryImpl;
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
 * @since : 11/26/22 Saturday
 **/
@Component
public class NotificationRepositoryImpl extends GenericMongoRepository<Notification> implements INotificationRepository {
    private final MongoDBClient<Notification> mongoDBClient;
    private static final Logger log = LogManager.getLogger(NotificationRepositoryImpl.class);

    @Autowired
    public NotificationRepositoryImpl(ENVConfig config) {
        mongoDBClient = new MongoDBClientImpl<>(MongoDBConfig.getInstance(MongoDBConfig
                .MongoDBConfigBuilder
                .config()
                .withConnectionURL(config.getStringProperty(Constant.KEY_CONFIG.DB))
                .withDatabaseName(Constant.DB_NAME.TRAVEL_SOCIAL_NETWORK).build()),
                config.getStringProperty(Constant.KEY_CONFIG.DB),
                Constant.DB_NAME.TRAVEL_SOCIAL_NETWORK,
                Constant.COLLECTION_NAME.NOTIFICATION,
                Notification.class);
    }

    @Override
    public MongoDBClient<Notification> getMongoDBOperator() {
        return mongoDBClient;
    }
}
