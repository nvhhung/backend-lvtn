package utils.mongodb;

import com.google.common.collect.Iterables;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <ORMClass>
 */
public class KijoKokoroMongoDBOperator<ORMClass> implements MongoDBOperator<ORMClass> {
    private MongoCollection<ORMClass> _collection;

    public KijoKokoroMongoDBOperator(KijiKokoroMongoDB mongoDB, String databaseURL, String dbName, String collection, Class<ORMClass> ormClass) {
        _collection = mongoDB.getMongoDatabase(databaseURL, dbName).getCollection(collection, ormClass);
    }

    @Override
    public ORMClass find(Document query, Document sort, Document projection) {
        return _collection.find(query).sort(sort).first();
    }

    @Override
    public ORMClass find(Bson query, Document sort, Document projection) {
        return _collection.find(query).sort(sort).first();
    }

    @Override
    public List<ORMClass> findMany(Document query, Bson sort, Document projection, int skips, int limit) {
        return _collection.find(query).sort(sort).projection(projection).skip(skips).limit(limit).into(new ArrayList<>());
    }

    @Override
    public List<ORMClass> findMany(Document query, Bson sort, int skips, int limit) {
        return _collection.find(query).sort(sort).skip(skips).limit(limit).into(new ArrayList<>());
    }

    @Override
    public List<ORMClass> findAll(Document query, Bson sort, Document projection) {
        return _collection.find(query).sort(sort).projection(projection).into(new ArrayList<>());
    }

    @Override
    public Long count(Document query) {
        return _collection.countDocuments(query);
    }

    @Override
    public Long count(Bson query) {
        return _collection.countDocuments(query);
    }

    @Override
    public ORMClass findOneAndUpdate(Document query, Document data) {
        return _collection.findOneAndUpdate(query, data, new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER));
    }

    @Override
    public UpdateResult update(Document query, Document data) {
        return _collection.updateOne(query, data);
    }

    @Override
    public ORMClass findOneAndUpsert(Document query, Document set, Document setOnInsert) {
        Document update = new Document("$set", set)
                .append("$setOnInsert", setOnInsert);
        return _collection.findOneAndUpdate(query, update, new FindOneAndUpdateOptions()
                .returnDocument(ReturnDocument.AFTER)
                .upsert(true));
    }

    @Override
    public ORMClass findOneAndUpsert(Document query, Document update) {
        return _collection.findOneAndUpdate(query, update, new FindOneAndUpdateOptions()
                .returnDocument(ReturnDocument.AFTER)
                .upsert(true));
    }

    @Override
    public UpdateResult upsert(Document query, Document data) {
        return _collection.updateOne(query, data, new UpdateOptions().upsert(true));
    }

    @Override
    public UpdateResult updateMany(Document query, Document data) {
        return _collection.updateMany(query, data);
    }

    @Override
    public UpdateResult updateMany(Document query, Document data, List<Document> arrayFilters) {
        if (CollectionUtils.isEmpty(arrayFilters)) {
            return this.updateMany(query, data);
        }
        return _collection.updateMany(query, data, new UpdateOptions().arrayFilters(arrayFilters));
    }

    @Override
    public ORMClass insert(ORMClass data) {
        _collection.insertOne(data);
        return data;
    }

    @Override
    public List<ORMClass> insertMany(List<ORMClass> data) {
        _collection.insertMany(data);
        return data;
    }

    @Override
    public AggregateIterable aggregateSpecial(List<Bson> pipeline) {
        return _collection.aggregate(pipeline, Document.class).allowDiskUse(true);
    }

    @Override
    public List<Object> aggregateSpecialList(List<Bson> pipeline) {
        return _collection.aggregate(pipeline, Document.class).allowDiskUse(true).into(new ArrayList());
    }

    @Override
    public List<ORMClass> aggregate(List<Bson> pipeline) {
        return _collection.aggregate(pipeline).allowDiskUse(true).into(new ArrayList<>());
    }

    @Override
    public ORMClass aggregateFind(List<Bson> pipeline) {
        return _collection.aggregate(pipeline).allowDiskUse(true).first();
    }

    @Override
    public Object aggregateFindObject(List<Bson> pipeline) {
        return _collection.aggregate(pipeline, Document.class).allowDiskUse(true).first();
    }

    @Override
    public Iterable<String> findDistinct(String fieldName, Bson queryString) {
        return _collection.distinct(fieldName, queryString, String.class).into(new ArrayList<>());
    }

    @Override
    public Integer countDistinct(String fieldName, Bson queryString) {
        return Iterables.size(_collection.distinct(fieldName, queryString, String.class).into(new ArrayList<>()));
    }

    @Override
    public DeleteResult delete(Bson queryString) {
        return _collection.deleteOne(queryString);
    }

}
