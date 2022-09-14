package hcmut.cse.travelsocialnetwork.service.elasticsearch;

import io.vertx.core.json.JsonObject;

import org.apache.http.HttpHost;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author : hung.nguyen23
 * @since : 9/13/22 Tuesday
 **/
public class ElasticsearchConfig {

    @Bean(destroyMethod = "close")
    public RestHighLevelClient restClient() {
        var elasticsearchCfg = new JsonObject("{\"host\":\"localhost\",\"port\":9200}");
        return new RestHighLevelClient(RestClient.builder(
                        new HttpHost(elasticsearchCfg.getString("host"), elasticsearchCfg.getInteger("port"), "http")
                )
                .setRequestConfigCallback(builder ->
                        builder.setConnectTimeout(5000)
                                .setSocketTimeout(30000))
                .setHttpClientConfigCallback(httpAsyncClientBuilder -> httpAsyncClientBuilder
                        .setDefaultIOReactorConfig(IOReactorConfig.custom()
                                .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                                .build())));
    }
}
