package hcmut.cse.travelsocialnetwork.service.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import eu.dozd.mongo.MongoMapper;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author : hung.nguyen23
 * @since : 8/1/22 Monday
 **/
@Component
public class MongoDBClient {
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public interface factory {
        MongoDBClient create();
    }

    @Autowired
    public MongoDBClient() {
    }

    private MongoDBClient(String connectionURL, String databaseName) {
        CodecRegistry codecRegistry = CodecRegistries.fromProviders(MongoMapper.getProviders());
        mongoClient = new MongoClient(new MongoClientURI(connectionURL));
        mongoDatabase = mongoClient.getDatabase(databaseName).withCodecRegistry(codecRegistry);
    }

    public static MongoDBClient getInstance(MongoDBConfigBuilder mongoDBConfigBuilder) {
        try{
            String key = getKeyMap
        }
    }

    public static final class MongoDBConfigBuilder {
        private String connectionURL;
        private String databaseName;

        private MongoDBConfigBuilder() {

        }

        public static MongoDBConfigBuilder config() {
            return new MongoDBConfigBuilder();
        }

        public MongoDBConfigBuilder withConnectionURL(String connectionURL) {
            this.connectionURL = connectionURL;
            return this;
        }

        public MongoDBConfigBuilder withDatabaseName(String databaseName) {
            this.databaseName = databaseName;
            return this;
        }

        public MongoDBConfigBuilder build() {
            return this;
        }
    }

    private static String getKeyMap(String url, String dbName) {
        try {
            return SHA512.valueOf(url + dbName);
        } catch (Throwable e) {
            e.printStackTrace();
            return UUID.fromString(url + dbName).toString();
        }
    }
}
