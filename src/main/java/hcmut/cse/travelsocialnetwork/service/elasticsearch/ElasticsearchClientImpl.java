package hcmut.cse.travelsocialnetwork.service.elasticsearch;

import io.vertx.core.json.JsonObject;
import org.apache.http.HttpHost;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Component;

/**
 * @author : hung.nguyen23
 * @since : 8/29/22 Monday
 **/
@Component
public class ElasticsearchClientImpl implements ElasticsearchClient{
    private static final Logger log = LogManager.getLogger(ElasticsearchClientImpl.class);
    private RestHighLevelClient restHighLevelClient;

    public ElasticsearchClientImpl() {
        var elasticsearchCfg = new JsonObject("{\"host\":\"localhost\",\"port\":9200}");
        restHighLevelClient = new RestHighLevelClient(RestClient.builder(getHttHostFromConfig(elasticsearchCfg))
                .setRequestConfigCallback(builder ->
                        builder.setConnectTimeout(5000)
                                .setSocketTimeout(30000))
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder
                        .setDefaultIOReactorConfig(IOReactorConfig.custom()
                                .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                                .build())));
    }

    private HttpHost getHttHostFromConfig(JsonObject config) {
        var host = HttpHost.create(config.getString("host") + ":" + config.getInteger("port"));
        return host;
    }


    @Override
    public String test() {
        return "use elasticsearch successful";
    }
}


