package hcmut.cse.travelsocialnetwork.service.elasticsearch;

import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;

import java.io.IOException;
import java.util.List;

/**
 * @author : hung.nguyen23
 * @since : 8/29/22 Monday
 **/
public interface VHElasticsearchClient {
    <T> boolean update(ParamElasticsearchObj params, Class<T> clazz) throws IOException;
    <T> GetResponse<T> get(ParamElasticsearchObj params, Class<T> clazz) throws IOException;
    <T> List<Hit<T>> searchDSL(ParamElasticsearchObj params, Class<T> clazz) throws IOException;
}
