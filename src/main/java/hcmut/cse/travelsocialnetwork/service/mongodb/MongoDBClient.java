package hcmut.cse.travelsocialnetwork.service.mongodb;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

/**
 * @author : hung.nguyen23
 * @since : 8/1/22 Monday
 **/
public interface MongoDBClient<ORMClass> {
    ORMClass find(Document query, Document sort, Document projection);

    ORMClass find(Bson query, Document sort, Document projection);

    List<ORMClass> findAll(Document query, Bson sort, Document projection);

    List<ORMClass> findMany(Document query, Bson sort, Document projection, int skips, int limit);

    List<ORMClass> findMany(Document query, Bson sort, int skips, int limit);

    Long count(Document query);

    Long count(Bson query);

    ORMClass findOneAndUpdate(Document query, Document data);

    ORMClass findOneAndUpsert(Document query, Document set, Document setOnInsert);

    ORMClass findOneAndUpsert(Document query, Document data);

    ORMClass insert(ORMClass data);

    List<ORMClass> insertMany(List<ORMClass> data);

    UpdateResult update(Document query, Document data);

    UpdateResult upsert(Document query, Document data);

    UpdateResult updateMany(Document query, Document data);

    UpdateResult updateMany(Document query, Document data, List<Document> arrayFilters);

    AggregateIterable<Document> aggregateSpecial(List<Bson> pipeline);

    List<Object> aggregateSpecialList(List<Bson> pipeline);

    List<ORMClass> aggregate(List<Bson> pipeline);

    ORMClass aggregateFind(List<Bson> pipeline);

    Object aggregateFindObject(List<Bson> pipeline);

    Iterable<String> findDistinct(String fieldName, Bson queryString);

    DeleteResult delete(Bson queryString);
}
