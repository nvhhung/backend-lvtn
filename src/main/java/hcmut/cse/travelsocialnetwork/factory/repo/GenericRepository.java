package hcmut.cse.travelsocialnetwork.factory.repo;

import hcmut.cse.travelsocialnetwork.service.mongodb.MongoDBClient;
import hcmut.cse.travelsocialnetwork.service.mongodb.MongoDBConfig;

/**
 * @author : hung.nguyen23
 * @since : 8/1/22 Monday
 **/
public interface GenericRepository<T> {
    MongoDBClient<T> getMongoDBOperator();
    
}
