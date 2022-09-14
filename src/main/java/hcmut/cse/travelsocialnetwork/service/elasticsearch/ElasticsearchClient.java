package hcmut.cse.travelsocialnetwork.service.elasticsearch;

import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import io.vertx.core.Handler;

import java.util.List;

/**
 * @author : hung.nguyen23
 * @since : 8/29/22 Monday
 **/
public interface ElasticsearchClient {
    String test();
    <T> void update(ParamElasticsearchObj params, Class<T> clazz, Handler<Boolean> callback);
    <T> void get(ParamElasticsearchObj params, Class<T> clazz, Handler<GetResponse<T>> callback);
    void insertNoId(ParamElasticsearchObj params, Handler<Boolean> callback);
    <T> void searchDSL(ParamElasticsearchObj params, Class<T> clazz, Handler<List<Hit<T>>> callback);
    <T> void searchByParams(ParamElasticsearchObj params, Class<T> clazz, Handler<List<Hit<T>>> callback);
    void deleteByQuery(ParamElasticsearchObj params, Handler<Long> callback);
}
