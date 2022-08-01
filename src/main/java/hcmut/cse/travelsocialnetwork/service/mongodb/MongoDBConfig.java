package hcmut.cse.travelsocialnetwork.service.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import eu.dozd.mongo.MongoMapper;
import hcmut.cse.travelsocialnetwork.utils.crypto.SHA512;
import org.bson.codecs.configuration.CodecRegistries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author : hung.nguyen23
 * @since : 8/1/22 Monday
 **/
@Component
public class MongoDBConfig {
    private static final Map<String, MongoDBConfig> providers = new HashMap<>();
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public interface factory {
        MongoDBConfig create();
    }

    @Autowired
    public MongoDBConfig() {
    }

    private MongoDBConfig(String connectionURL, String databaseName) {
        var codecRegistry = CodecRegistries.fromProviders(MongoMapper.getProviders());
        mongoClient = new MongoClient(new MongoClientURI(connectionURL));
        mongoDatabase = mongoClient.getDatabase(databaseName).withCodecRegistry(codecRegistry);
    }

  public static MongoDBConfig getInstance(MongoDBConfigBuilder mongoDBConfigBuilder) {
        var key = getKeyMap(mongoDBConfigBuilder.connectionURL, mongoDBConfigBuilder.databaseName);
        if (!providers.containsKey(key)) {
            registerProvider(mongoDBConfigBuilder.connectionURL, mongoDBConfigBuilder.databaseName);
        }
        return providers.get(key);
  }

    private static void registerProvider(String connectionURL, String databaseName) {
        var key = getKeyMap(connectionURL, databaseName);
        if (!providers.containsKey(key)) {
            providers.put(key, new MongoDBConfig(connectionURL, databaseName));
        }
    }

    MongoDatabase getMongoDatabase(String connectionURL, String dataBaseName) {
        return providers.get(getKeyMap(connectionURL, dataBaseName)).mongoDatabase;
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
