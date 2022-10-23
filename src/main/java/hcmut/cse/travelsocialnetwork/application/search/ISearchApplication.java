package hcmut.cse.travelsocialnetwork.application.search;

import co.elastic.clients.elasticsearch.core.search.Hit;
import hcmut.cse.travelsocialnetwork.service.elasticsearch.ParamElasticsearchObj;

import java.io.IOException;
import java.util.List;

/**
 * @author : hung.nguyen23
 * @since : 9/27/22 Tuesday
 **/
public interface ISearchApplication {
    List<Hit> searchES(ParamElasticsearchObj paramElasticsearchObj) throws IOException;
}
