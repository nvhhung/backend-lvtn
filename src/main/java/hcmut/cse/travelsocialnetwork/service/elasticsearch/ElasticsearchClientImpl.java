package hcmut.cse.travelsocialnetwork.service.elasticsearch;
import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch._types.*;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpDeserializer;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.SimpleJsonpMapper;
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

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static co.elastic.clients.elasticsearch._types.Conflicts.Proceed;

/**
 * @author : hung.nguyen23
 * @since : 8/29/22 Monday
 **/
@Component
public class ElasticsearchClientImpl implements ElasticsearchClient{
    private static final Logger log = LogManager.getLogger(ElasticsearchClientImpl.class);
    private final ElasticsearchAsyncClient client;
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
        client = new ElasticsearchAsyncClient(transport);
        log.info("connect elasticsearch successfully");
    }

    @Override
    public String test() {
        return "use elasticsearch successful";
    }

    @Override
    public <T> void update(ParamElasticsearchObj params, Class<T> clazz, Handler<Boolean> callback) {
        client.update(updateRequest ->
                        updateRequest.index(params.getIndex())
                                .id(params.getId())
                                .docAsUpsert(params.isUpsert())
                                .doc(buildJsonData(params.getJBody().toString()))
                                .detectNoop(false)
                , clazz
        ).whenCompleteAsync((response, throwable) -> {
            if (response == null) {
                log.warn(EXCEPTION_ELASTICSEARCH);
                throwable.printStackTrace();
                callback.handle(null);
                return;
            }
            switch (response.result()) {
                case Created -> {
                    log.info("Insert success doc have id: " + params.getId() + " in index: " + params.getIndex());
                    callback.handle(true);
                }
                case Updated -> {
                    log.info("Update success doc have id: " + params.getId() + " in index: " + params.getIndex());
                    callback.handle(true);
                }
                case NotFound -> {
                    log.info("Id not exist in index: " + params.getIndex());
                    callback.handle(false);
                }
                case NoOp -> {
                    log.info("No operation with elasticsearch in index: " + params.getIndex());
                    callback.handle(false);
                }
                default -> {
                    log.info("Response no have result");
                    callback.handle(false);
                }
            }
        });
    }

    private JsonData buildJsonData(String joBody) {
        JsonpMapper mapper = SimpleJsonpMapper.INSTANCE_REJECT_UNKNOWN_FIELDS;
        JsonpDeserializer<JsonData> tDocumentDeserializer = JsonData._DESERIALIZER;
        return tDocumentDeserializer.deserialize(mapper.jsonProvider().createParser(new StringReader(joBody)), mapper);
    }

    @Override
    public <T> void get(ParamElasticsearchObj params, Class<T> clazz, Handler<GetResponse<T>> callback) {
        client.get(getRequest -> getRequest
                                .index(params.getIndex())
                                .id(params.getId()),
                        clazz
                )
                .whenCompleteAsync((response, throwable) -> {
                            if (response == null) {
                                log.warn(EXCEPTION_ELASTICSEARCH);
                                throwable.printStackTrace();
                                callback.handle(null);
                                return;
                            }
                            if (!response.found()) {
                                log.info("id = " + params.getId() + " not exist");
                                callback.handle(null);
                                return;
                            }
                            callback.handle(response);
                        }
                );
    }

    @Override
    public void insertNoId(ParamElasticsearchObj params, Handler<Boolean> callback) {
        client.index(insertRequest ->
                        insertRequest
                                .index(params.getIndex())
                                .withJson(new StringReader(params.getJBody().toString())))
                .whenCompleteAsync((response, throwable) -> {
                    if (response == null) {
                        log.warn(EXCEPTION_ELASTICSEARCH);
                        throwable.printStackTrace();
                        callback.handle(null);
                        return;
                    }
                    if (response.result() != Result.Created) {
                        log.info("Insert doc failure");
                        callback.handle(false);
                        return;
                    }
                    callback.handle(true);
                });
    }

    @Override
    public <T> void searchDSL(ParamElasticsearchObj params, Class<T> clazz, Handler<List<Hit<T>>> callback) {
        client.search(searchRequest -> searchRequest
                        .index(params.getIndex())
                        .from(params.getFrom())
                        .size(params.getSize())
                        .minScore((double) params.getMinScore())
                        .sort(buildListSortOptions(params))
                        .query(QueryBuilders.wrapper(wrapperQuery -> wrapperQuery
                                .query(Base64.getEncoder()
                                        .encodeToString(params.getQuery().getBytes(StandardCharsets.UTF_8)))
                        ))
                , clazz
        ).whenCompleteAsync((response, throwable) -> {
            if (response == null) {
                log.error(EXCEPTION_ELASTICSEARCH);
                throwable.printStackTrace();
                callback.handle(null);
                return;
            }
            callback.handle(response.hits().hits());
        });
    }

    private List<SortOptions> buildListSortOptions(ParamElasticsearchObj params) {
        if (params.getSorts() != null) {
            return params.getSorts().stream().map(SortData::sortBuilder).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return List.of(SortOptionsBuilders.geoDistance(distance -> distance
                .field("LOCATION")
                .location(location -> location.latlon(coordinates -> coordinates
                        .lat(params.getLat())
                        .lon(params.getLon()))
                )
                .distanceType(GeoDistanceType.Plane)
                .order(SortOrder.Asc)
                .unit(DistanceUnit.Meters)
                .mode(SortMode.Min)));
    }

    @Override
    public <T> void searchByParams(ParamElasticsearchObj params, Class<T> clazz, Handler<List<Hit<T>>> callback) {
        client.search(searchRequest ->
                        searchRequest.index(params.getIndex())
                                .from(params.getFrom())
                                .size(params.getSize())
                                .query(params.getKey() != null ? QueryBuilders.terms(termQuery ->
                                        termQuery.field(params.getKey())
                                                .terms(termQueryField -> termQueryField.value(List.of(FieldValue.of(params.getValue())))))
                                        : null)
                , clazz
        ).whenCompleteAsync((response, throwable) -> {
            if (response == null) {
                log.warn(EXCEPTION_ELASTICSEARCH);
                throwable.printStackTrace();
                callback.handle(null);
                return;
            }

            callback.handle(response.hits().hits());
        });
    }

    @Override
    public void deleteByQuery(ParamElasticsearchObj params, Handler<Long> callback) {
        client.deleteByQuery(deleteRequest ->
                        deleteRequest.index(String.join(",", params.getIndexList()))
                                .query(QueryBuilders.wrapper(wrapperQuery ->
                                        wrapperQuery.query(Base64.getEncoder().encodeToString(params.getQuery()
                                                .getBytes(StandardCharsets.UTF_8))
                                        )))
                                .conflicts(Proceed))
                .whenCompleteAsync((response, throwable) -> {
                    if (response == null) {
                        log.info("job delete fail");
                        callback.handle(0L);
                        return;
                    }
                    log.info("response delete: " + response);
                    callback.handle(response.deleted());
                });
    }
}


