package hcmut.cse.travelsocialnetwork.service.mongodb;

/**
 * @author : hung.nguyen23
 * @since : 8/1/22 Monday
 **/

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

/**
 * @param <ORMClass>
 */
public class MongoDBClient<ORMClass> implements MongoDBOperator<ORMClass> {
    private MongoCollection<ORMClass> collection;
    public MongoDBClient()

    @Override
    public ORMClass find(Document query, Document sort, Document projection) {
        return null;
    }

    @Override
    public ORMClass find(Bson query, Document sort, Document projection) {
        return null;
    }

    @Override
    public List<ORMClass> findAll(Document query, Bson sort, Document projection) {
        return null;
    }

    @Override
    public List<ORMClass> findMany(Document query, Bson sort, Document projection, int skips, int limit) {
        return null;
    }

    @Override
    public List<ORMClass> findMany(Document query, Bson sort, int skips, int limit) {
        return null;
    }

    @Override
    public Long count(Document query) {
        return null;
    }

    @Override
    public Long count(Bson query) {
        return null;
    }

    @Override
    public ORMClass findOneAndUpdate(Document query, Document data) {
        return null;
    }

    @Override
    public ORMClass findOneAndUpsert(Document query, Document set, Document setOnInsert) {
        return null;
    }

    @Override
    public ORMClass findOneAndUpsert(Document query, Document data) {
        return null;
    }

    @Override
    public ORMClass insert(ORMClass data) {
        return null;
    }

    @Override
    public List<ORMClass> insertMany(List<ORMClass> data) {
        return null;
    }

    @Override
    public UpdateResult update(Document query, Document data) {
        return null;
    }

    @Override
    public UpdateResult upsert(Document query, Document data) {
        return null;
    }

    @Override
    public UpdateResult updateMany(Document query, Document data) {
        return null;
    }

    @Override
    public UpdateResult updateMany(Document query, Document data, List<Document> arrayFilters) {
        return null;
    }

    @Override
    public AggregateIterable<Document> aggregateSpecial(List<Bson> pipeline) {
        return null;
    }

    @Override
    public List<Object> aggregateSpecialList(List<Bson> pipeline) {
        return null;
    }

    @Override
    public List<ORMClass> aggregate(List<Bson> pipeline) {
        return null;
    }

    @Override
    public ORMClass aggregateFind(List<Bson> pipeline) {
        return null;
    }

    @Override
    public Object aggregateFindObject(List<Bson> pipeline) {
        return null;
    }

    @Override
    public Iterable<String> findDistinct(String fieldName, Bson queryString) {
        return null;
    }

    @Override
    public Integer countDistinct(String fieldName, Bson queryString) {
        return null;
    }

    @Override
    public DeleteResult delete(Bson queryString) {
        return null;
    }
}
