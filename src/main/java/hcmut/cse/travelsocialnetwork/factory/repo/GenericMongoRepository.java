package hcmut.cse.travelsocialnetwork.factory.repo;

import hcmut.cse.travelsocialnetwork.utils.Constant;
import lombok.NonNull;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author : hung.nguyen23
 * @since : 8/1/22 Monday
 **/
public abstract class GenericMongoRepository<T extends PO> implements GenericRepository<T> {
    @Override
    public Optional<Long> count(@NonNull Map<String, Object> queryString) {
        try {
            Document query = new Document(queryString);
            return Optional.of(getMongoDBOperator().count(query));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return Optional.of(0L);
        }
    }

    @Override
    public Optional<List<T>> search(@NonNull Map<String, Object> queryString, @NonNull Map<String, Object> querySort, int page, int size) {
        try {
            Document query = new Document(queryString);
            query.put(Constant.FIELD.IS_DELETED, false);
            Document sort = new Document(querySort);
            List<T> list = getMongoDBOperator().findMany(query, sort, new Document(), (page - 1) * size, size);
            if (!CollectionUtils.isEmpty(list)) {
                return Optional.of(list);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<T>> search(@NonNull Map<String, Object> queryString, @NonNull Map<String, Object> querySort,
                                    @NonNull Map<String, Object> queryProjection, int page, int size) {
        try {
            Document query = new Document(queryString);
            query.put(Constant.FIELD.IS_DELETED, false);
            Document sort = new Document(querySort);
            Document projection = new Document(queryProjection);
            List<T> list = getMongoDBOperator().findMany(query, sort, projection, (page - 1) * size, size);
            if (!CollectionUtils.isEmpty(list)) {
                return Optional.of(list);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<T>> searchWithDeleted(@NonNull Map<String, Object> queryString, @NonNull Map<String, Object> querySort, int page, int size) {
        try {
            Document query = new Document(queryString);
            Document sort = new Document(querySort);
            List<T> list = getMongoDBOperator().findMany(query, sort, new Document(), (page - 1) * size, size);
            if (!CollectionUtils.isEmpty(list)) {
                return Optional.of(list);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<T> getById(@NonNull String id) {
        try {
            return Optional.ofNullable(getMongoDBOperator().find(new Document(Constant.FIELD.ID, new ObjectId(id))
                    .append(Constant.FIELD.IS_DELETED, false), new Document(), new Document()));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<T> getByIdWithDeleted(@NonNull String id) {
        try {
            return Optional.ofNullable(getMongoDBOperator().find(new Document(Constant.FIELD.ID, new ObjectId(id)),
                    new Document(), new Document()));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<T> get(@NonNull Map<String, Object> query) {
        try {
            query.putIfAbsent(Constant.FIELD.IS_DELETED, false);
            List<T> list = getMongoDBOperator().findMany(new Document(query), new Document(), new Document(), 0, 1);
            if (!CollectionUtils.isEmpty(list)) {
                return Optional.of(list.get(0));
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<T> add(@NonNull T t) {
        try {
            t.setCreateTime(System.currentTimeMillis());
            t.setLastUpdateTime(System.currentTimeMillis());
            t.setIsDeleted(false);
            return Optional.of(getMongoDBOperator().insert(t));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<T> addNoneTime(@NonNull T t) {
        try {
            return Optional.of(getMongoDBOperator().insert(t));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<T>> addMany(@NonNull List<T> t) {
        try {
            return Optional.of(getMongoDBOperator().insertMany(t));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Boolean> addManyV2(@NonNull List<T> t) {
        try {
            getMongoDBOperator().insertMany(t);
            return Optional.of(true);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return Optional.of(false);
        }
    }

    @Override
    public Optional<T> update(@NonNull String id, @NonNull T t) {
        try {
            Document query = new Document(Constant.FIELD.ID, new ObjectId(id));
            query.append(Constant.FIELD.IS_DELETED, false);
            t.setLastUpdateTime(System.currentTimeMillis());
            Document data = BuildQuerySet.buildQuerySet(t);
            return Optional.ofNullable(getMongoDBOperator().findOneAndUpdate(query, data));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<T> update(@NonNull Map<String, Object> query, @NonNull T t) {
        try {
            Document data = BuildQuerySet.buildQuerySet(t);
            return Optional.ofNullable(getMongoDBOperator().findOneAndUpdate(new Document(query), data));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Boolean> update(@NonNull Map<String, Object> queryString, @NonNull Map<String, Object> updateString) {
        try {
            Document query = new Document(queryString);
            Document data = new Document(updateString);
            return Optional.of(getMongoDBOperator().updateMany(query, data).getModifiedCount() >= 1);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Boolean> delete(@NonNull String id) {
        try {
            Document query = new Document(Constant.FIELD.ID, new ObjectId(id));
            Document data = new Document(Constant.OPERATOR_MONGODB.SET, new Document(Constant.FIELD.IS_DELETED, true)
                    .append(Constant.FIELD.LAST_UPDATE_TIME, System.currentTimeMillis()));
            return Optional.of(getMongoDBOperator().update(query, data).getModifiedCount() == 1);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return Optional.of(false);
        }
    }

    @Override
    public Optional<Boolean> deleteMany(@NonNull Map<String, Object> queryString) {
        try {
            Document query = new Document(queryString);
            Document data = new Document(Constant.OPERATOR_MONGODB.SET, new Document(Constant.FIELD.IS_DELETED, true)
                    .append(Constant.FIELD.LAST_UPDATE_TIME, System.currentTimeMillis()));
            return Optional.of(getMongoDBOperator().updateMany(query, data).getModifiedCount() == 1);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return Optional.of(false);
        }
    }

    @Override
    public Optional<T> upsert(@NonNull Map<String, Object> queryString, @NonNull T t, @NonNull Map<String, Object> setOnInsert) {
        try {
            Document query = new Document(queryString);
            query.append(Constant.FIELD.IS_DELETED, false);
            t.setLastUpdateTime(System.currentTimeMillis());
            Document data = BuildQuerySet.buildQueryUpsert(t);
            return Optional.ofNullable(getMongoDBOperator().findOneAndUpsert(query, data, new Document(setOnInsert)));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<T> addToSet(@NonNull Map<String, Object> queryString, @NonNull Map<String, Object> addToSetMap) {
        try {
            Document query = new Document(queryString);
            Map<String, Object> updateData = new HashMap<>();
            updateData.put(Constant.OPERATOR_MONGODB.ADD_TO_SET, addToSetMap);
            Document data = new Document(updateData);
            return Optional.of(getMongoDBOperator().findOneAndUpsert(query, data));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return Optional.empty();
    }
}
