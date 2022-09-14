package hcmut.cse.travelsocialnetwork.service.elasticsearch;
import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import org.apache.http.HttpHost;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : hung.nguyen23
 * @since : 8/29/22 Monday
 **/
@Component
public class ElasticsearchClientImpl implements ElasticsearchClient{
    private static final Logger log = LogManager.getLogger(ElasticsearchClientImpl.class);
    private ElasticsearchAsyncClient client;

    @Autowired
    public ElasticsearchClientImpl() {
        var elasticsearchCfg = new JsonObject("{\"host\":\"localhost\",\"port\":9200}");
        var restClient = RestClient.builder(new HttpHost(elasticsearchCfg.getString("host"), elasticsearchCfg.getInteger("port"), "http"))
                .setRequestConfigCallback(builder ->
                        builder.setConnectTimeout(5000)
                                .setSocketTimeout(30000))
                .setHttpClientConfigCallback(httpAsyncClientBuilder ->
                        httpAsyncClientBuilder.setDefaultIOReactorConfig(IOReactorConfig
                                .custom()
                                .setIoThreadCount(Runtime
                                        .getRuntime()
                                        .availableProcessors())
                                .build()
                        )
                )
                .build();
        var transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        client = new ElasticsearchAsyncClient(transport);
        log.info("connect elasticsearch successfully");
    }

    @Override
    public String test() {
        return "use elasticsearch successful";
    }

    @Override
    public <T> void update(ParamElasticsearchObj params, Class<T> clazz, Handler<Boolean> callback) {

    }

    @Override
    public <T> void get(ParamElasticsearchObj params, Class<T> clazz, Handler<GetResponse<T>> callback) {

    }

    @Override
    public void insertNoId(ParamElasticsearchObj params, Handler<Boolean> callback) {

    }

    @Override
    public <T> void searchDSL(ParamElasticsearchObj params, Class<T> clazz, Handler<List<Hit<T>>> callback) {

    }

    @Override
    public <T> void searchByParams(ParamElasticsearchObj params, Class<T> clazz, Handler<List<Hit<T>>> callback) {

    }

    @Override
    public void deleteByQuery(ParamElasticsearchObj params, Handler<Long> callback) {

    }
}


