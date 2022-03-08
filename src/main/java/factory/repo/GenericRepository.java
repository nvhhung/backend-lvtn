package factory.repo;

import lombok.NonNull;
import utils.mongodb.MongoDBOperator;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * An interface for OMI entity database operators
 *
 * @param <T>
 * @version 1.0
 */
public interface GenericRepository<T> {
    MongoDBOperator<T> getMongoDBOperator();

    /* SELECT */
    Optional<Long> count(@NonNull Map<String, Object> query);

    Optional<List<T>> search(@NonNull Map<String, Object> query, @NonNull Map<String, Object> sort, int page, int size);

    Optional<List<T>> search(@NonNull Map<String, Object> query, @NonNull Map<String, Object> sort, @NonNull Map<String, Object> projection, int page, int size);

    Optional<List<T>> searchWithDeleted(@NonNull Map<String, Object> query, @NonNull Map<String, Object> sort, int page, int size);

    Optional<T> getById(@NonNull String id);

    Optional<T> getByIdWithDeleted(@NonNull String id);

    Optional<T> get(@NonNull Map<String, Object> query);


    /* INSERT */
    Optional<T> add(@NonNull T t);

    Optional<T> addNoneTime(@NonNull T t);

    Optional<List<T>> addMany(@NonNull List<T> t);

    Optional<Boolean> addManyV2(@NonNull List<T> t);

    Optional<T> update(@NonNull String id, @NonNull T t);

    Optional<T> update(@NonNull Map<String, Object> query, @NonNull T t);

    Optional<Boolean> update(@NonNull Map<String, Object> queryString, @NonNull Map<String, Object> updateString);

    Optional<Boolean> delete(@NonNull String id);

    Optional<T> upsert(@NonNull Map<String, Object> queryString, @NonNull T t, @NonNull Map<String, Object> setOnInsert);

    Optional<T> addToSet(@NonNull Map<String, Object> queryString, @NonNull Map<String, Object> addToSetMap);

}
