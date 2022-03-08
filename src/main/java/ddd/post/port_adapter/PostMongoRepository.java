package ddd.post.port_adapter;

import configuration.ENVConfig;
import ddd.post.Post;
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

@Component
public class PostMongoRepository extends GenericMongoRepository<Post> implements IPostRepository {
    private final MongoDBOperator<Post> mongoDBOperator;
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    public PostMongoRepository(ENVConfig applicationConfig) {
        mongoDBOperator = new KijoKokoroMongoDBOperator<>(
                KijiKokoroMongoDB.getInstance(KijiKokoroMongoDB.MongoDBConfigBuilder.config()
                        .withConnectionURL(applicationConfig.getStringProperty(DbNameEnum.KEY))
                        .withDatabaseName(DbNameEnum.USER).build()),
                applicationConfig.getStringProperty(DbNameEnum.KEY),
                DbNameEnum.USER, CollectionEnum.POST, Post.class
        );
    }

    @Override
    public MongoDBOperator<Post> getMongoDBOperator() {
        return mongoDBOperator;
    }
}
