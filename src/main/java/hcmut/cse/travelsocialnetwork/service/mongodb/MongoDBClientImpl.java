package hcmut.cse.travelsocialnetwork.service.mongodb;

/**
 * @author : hung.nguyen23
 * @since : 8/1/22 Monday
 **/

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <ORMClass>
 */
public class MongoDBClientImpl<ORMClass> implements MongoDBClient<ORMClass> {
    private final MongoCollection<ORMClass> operator;

    public MongoDBClientImpl(MongoDBConfig mongoDBConfig, String databaseURL, String dbName, String collection, Class<ORMClass> ormClass) {
        operator = mongoDBConfig.getMongoDatabase(databaseURL, dbName).getCollection(collection, ormClass);
    }

    @Override
    public ORMClass find(Document query, Document sort, Document projection) {
        return operator.find(query).sort(sort).first();
    }

    @Override
    public ORMClass find(Bson query, Document sort, Document projection) {
        return operator.find(query).sort(sort).first();
    }

    @Override
    public List<ORMClass> findAll(Document query, Bson sort, Document projection) {
        return operator.find(query).sort(sort).projection(projection).into(new ArrayList<>());
    }

    @Override
    public List<ORMClass> findMany(Document query, Bson sort, Document projection, int skips, int limit) {
        return operator.find(query).sort(sort).projection(projection).skip(skips).limit(limit).into(new ArrayList<>());
    }

    @Override
    public List<ORMClass> findMany(Document query, Bson sort, int skips, int limit) {
        return operator.find(query).sort(sort).skip(skips).limit(limit).into(new ArrayList<>());
    }

    @Override
    public Long count(Document query) {
        return operator.countDocuments(query);
    }

    @Override
    public Long count(Bson query) {
        return operator.countDocuments(query);
    }

    @Override
    public ORMClass findOneAndUpdate(Document query, Document data) {
        return operator.findOneAndUpdate(query, data, new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER));
    }

    @Override
    public ORMClass findOneAndUpsert(Document query, Document set, Document setOnInsert) {
        Document update = new Document("$set", set)
                .append("$setOnInsert", setOnInsert);
        return operator.findOneAndUpdate(query, update, new FindOneAndUpdateOptions()
                .returnDocument(ReturnDocument.AFTER)
                .upsert(true));
    }

    @Override
    public ORMClass findOneAndUpsert(Document query, Document data) {
        return operator.findOneAndUpdate(query, data, new FindOneAndUpdateOptions()
                .returnDocument(ReturnDocument.AFTER)
                .upsert(true));
    }

    @Override
    public ORMClass insert(ORMClass data) {
        operator.insertOne(data);
        return data;
    }

    @Override
    public List<ORMClass> insertMany(List<ORMClass> data) {
        operator.insertMany(data);
        return data;
    }

    @Override
    public UpdateResult update(Document query, Document data) {
        return operator.updateOne(query, data);
    }

    @Override
    public UpdateResult upsert(Document query, Document data) {
        return operator.updateOne(query, data, new UpdateOptions().upsert(true));
    }

    @Override
    public UpdateResult updateMany(Document query, Document data) {
        return operator.updateMany(query, data);
    }

    @Override
    public UpdateResult updateMany(Document query, Document data, List<Document> arrayFilters) {
        if (CollectionUtils.isEmpty(arrayFilters)) {
            return this.updateMany(query, data);
        }
        return operator.updateMany(query, data, new UpdateOptions().arrayFilters(arrayFilters));
    }

    @Override
    public AggregateIterable<Document> aggregateSpecial(List<Bson> pipeline) {
        return operator.aggregate(pipeline, Document.class).allowDiskUse(true);
    }

    @Override
    public List<Object> aggregateSpecialList(List<Bson> pipeline) {
        return operator.aggregate(pipeline, Document.class).allowDiskUse(true).into(new ArrayList());
    }

    @Override
    public List<ORMClass> aggregate(List<Bson> pipeline) {
        return operator.aggregate(pipeline).allowDiskUse(true).into(new ArrayList<>());
    }

    @Override
    public ORMClass aggregateFind(List<Bson> pipeline) {
        return operator.aggregate(pipeline).allowDiskUse(true).first();
    }

    @Override
    public Object aggregateFindObject(List<Bson> pipeline) {
        return operator.aggregate(pipeline, Document.class).allowDiskUse(true).first();
    }

    @Override
    public Iterable<String> findDistinct(String fieldName, Bson queryString) {
        return operator.distinct(fieldName, queryString, String.class).into(new ArrayList<>());
    }

    @Override
    public DeleteResult delete(Bson queryString) {
        return operator.deleteOne(queryString);
    }
}
