package utils.mongodb.v2;//package utils.mongodb.v2;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import com.google.inject.Singleton;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.model.FindOneAndUpdateOptions;
//import com.mongodb.client.model.ReturnDocument;
//import com.mongodb.client.model.UpdateOptions;
//import com.mongodb.client.result.UpdateResult;
//import org.bson.Document;
//import org.bson.conversions.Bson;
//import utils.mongodb.MongoDBOperator;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @param <ORMClass>
// */
//@Singleton
//public class KijiKokoroMongoDB<ORMClass> implements MongoDBOperator<ORMClass> {
//    private MongoCollection<ORMClass> _collection;
//
//    @Autowired
//    public KijiKokoroMongoDB(utils.mongodb.KijiKokoroMongoDB mongoDB, String databaseURL, String dbName, String collection, Class<ORMClass> ormClass) {
//        _collection = mongoDB.getMongoDatabase(databaseURL, dbName).getCollection(collection, ormClass);
//    }
//
//    @Override
//    public ORMClass find(Document query, Document sort, Document projection) {
//        return _collection.find(query).first();
//    }
//
//    @Override
//    public List<ORMClass> findMany(Document query, Bson sort, Document projection, int skips, int limit) {
//        return _collection.find(query).sort(sort).projection(projection).skip(skips).limit(limit).into(new ArrayList<>());
//    }
//
//    @Override
//    public Long count(Document query) {
//        return _collection.countDocuments(query);
//    }
//
//    @Override
//    public ORMClass findOneAndUpdate(Document query, Document data) {
//        return _collection.findOneAndUpdate(query, data, new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER));
//    }
//
//    @Override
//    public UpdateResult update(Document query, Document data) {
//        return _collection.updateOne(query, data);
//    }
//
//    @Override
//    public ORMClass findOneAndUpsert(Document query, Document set, Document setOnInsert) {
//        Document update = new Document("$set", setOnInsert)
//                .append("$setOnInsert", setOnInsert);
//        return _collection.findOneAndUpdate(query, update, new FindOneAndUpdateOptions()
//                .returnDocument(ReturnDocument.AFTER)
//                .upsert(true));
//    }
//
//    @Override
//    public ORMClass findOneAndUpsert(Document query, Document update) {
//        return _collection.findOneAndUpdate(query, update, new FindOneAndUpdateOptions()
//                .returnDocument(ReturnDocument.AFTER)
//                .upsert(true));
//    }
//
//    @Override
//    public UpdateResult upsert(Document query, Document data) {
//        return _collection.updateOne(query, data, new UpdateOptions().upsert(true));
//    }
//
//    @Override
//    public UpdateResult updateMany(Document query, Document data) {
//        return _collection.updateMany(query, data);
//    }
//
//
//    @Override
//    public ORMClass insert(ORMClass data) {
//        _collection.insertOne(data);
//        return data;
//    }
//
//    @Override
//    public List<ORMClass> insertMany(List<ORMClass> data) {
//        _collection.insertMany(data);
//        return data;
//    }
//}
