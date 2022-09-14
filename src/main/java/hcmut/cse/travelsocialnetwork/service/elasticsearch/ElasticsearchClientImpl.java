package hcmut.cse.travelsocialnetwork.service.elasticsearch;

import io.vertx.core.json.JsonObject;
import org.apache.http.HttpHost;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author : hung.nguyen23
 * @since : 8/29/22 Monday
 **/
public class ElasticsearchClientImpl implements ElasticsearchClient{
    private static final Logger log = LogManager.getLogger(ElasticsearchClientImpl.class);
    private RestHighLevelClient client;


    @Autowired
    public ElasticsearchClientImpl(RestHighLevelClient client) {
       this.client = client;
    }

    @Override
    public String test() {
        return "use elasticsearch successful";
    }
}


