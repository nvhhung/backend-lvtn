package hcmut.cse.travelsocialnetwork.repository.comment;

import hcmut.cse.travelsocialnetwork.factory.configuration.ENVConfig;
import hcmut.cse.travelsocialnetwork.factory.repo.GenericMongoRepository;
import hcmut.cse.travelsocialnetwork.model.Comment;
import hcmut.cse.travelsocialnetwork.model.Post;
import hcmut.cse.travelsocialnetwork.service.mongodb.MongoDBClient;
import hcmut.cse.travelsocialnetwork.service.mongodb.MongoDBClientImpl;
import hcmut.cse.travelsocialnetwork.service.mongodb.MongoDBConfig;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : hung.nguyen23
 * @since : 9/7/22 Wednesday
 **/
public class CommentRepositoryImpl extends GenericMongoRepository<Comment> implements ICommentRepository{
    private final MongoDBClient<Comment> mongoDBClient;
    private static final Logger log = LogManager.getLogger(CommentRepositoryImpl.class);

    @Autowired
    public CommentRepositoryImpl(ENVConfig config) {
        mongoDBClient = new MongoDBClientImpl<>(MongoDBConfig.getInstance(MongoDBConfig
                .MongoDBConfigBuilder
                .config()
                .withConnectionURL(config.getStringProperty(Constant.KEY_CONFIG.DB))
                .withDatabaseName(Constant.DB_NAME.TRAVEL_SOCIAL_NETWORK).build()),
                config.getStringProperty(Constant.KEY_CONFIG.DB),
                Constant.DB_NAME.TRAVEL_SOCIAL_NETWORK,
                Constant.COLLECTION_NAME.COMMENT,
                Comment.class);
    }

    @Override
    public MongoDBClient<Comment> getMongoDBOperator() {
        return mongoDBClient;
    }
}
