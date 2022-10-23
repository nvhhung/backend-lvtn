package hcmut.cse.travelsocialnetwork.service.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpDeserializer;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.SimpleJsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import io.vertx.core.json.JsonObject;
import org.apache.http.HttpHost;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

/**
 * @author : hung.nguyen23
 * @since : 8/29/22 Monday
 **/
@Component
public class ElasticsearchClientImpl implements VHElasticsearchClient {
    private static final Logger log = LogManager.getLogger(ElasticsearchClientImpl.class);
    private final ElasticsearchClient client;
    private static final String EXCEPTION_ELASTICSEARCH = "Have exception when call elasticsearch";

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
        client = new ElasticsearchClient(transport);
        log.info("connect elasticsearch successfully");
    }

    @Override
    public <T> boolean update(ParamElasticsearchObj params, Class<T> clazz) throws IOException {
        var updateResponse = client.update(updateRequest ->
                        updateRequest.index(params.getIndex())
                                .id(params.getId())
                                .docAsUpsert(params.isUpsert())
                                .doc(buildJsonData(params.getJBody().toString()))
                                .detectNoop(false), clazz);
        if (updateResponse == null) {
            log.warn(EXCEPTION_ELASTICSEARCH);
            return false;
        }
        switch (updateResponse.result()) {
            case Created -> {
                log.info("Insert success doc have id: " + params.getId() + " in index: " + params.getIndex());
                return true;
            }
            case Updated -> {
                log.info("Update success doc have id: " + params.getId() + " in index: " + params.getIndex());
                return true;
            }
            case NotFound -> {
                log.info("Id not exist in index: " + params.getIndex());
                return false;
            }
            case NoOp -> {
                log.info("No operation with elasticsearch in index: " + params.getIndex());
                return false;
            }
            default -> {
                log.info("Response no have result");
                return false;
            }
        }
    }

    private JsonData buildJsonData(String joBody) {
        JsonpMapper mapper = SimpleJsonpMapper.INSTANCE_REJECT_UNKNOWN_FIELDS;
        JsonpDeserializer<JsonData> tDocumentDeserializer = JsonData._DESERIALIZER;
        return tDocumentDeserializer.deserialize(mapper.jsonProvider().createParser(new StringReader(joBody)), mapper);
    }

    @Override
    public <T> GetResponse<T> get(ParamElasticsearchObj params, Class<T> clazz) throws IOException {
        var getResponse = client.get(getRequest -> getRequest
                                .index(params.getIndex())
                                .id(params.getId()),
                        clazz);
        if (getResponse == null) {
            log.warn(EXCEPTION_ELASTICSEARCH);
            return null;
        }
        if (!getResponse.found()) {
            log.info("id = " + params.getId() + " not exist");
            return null;
        }
        return getResponse;
    }

    @Override
    public <T> List<Hit<T>> searchDSL(ParamElasticsearchObj params, Class<T> clazz) throws IOException {
        var searchResponse = client.search(searchRequest -> searchRequest
                        .index(params.getIndex())
                        .from(params.getFrom())
                        .size(params.getSize())
                        .minScore((double) params.getMinScore())
                        .query(QueryBuilders.wrapper(wrapperQuery -> wrapperQuery
                                .query(Base64.getEncoder()
                                        .encodeToString(params.getQuery().getBytes(StandardCharsets.UTF_8)))
                        ))
                , clazz);
        if (searchResponse == null) {
            log.error(EXCEPTION_ELASTICSEARCH);
            return null;
        }
        log.info("search have size: " + searchResponse.hits().hits().size());
        return searchResponse.hits().hits();
    }
}


