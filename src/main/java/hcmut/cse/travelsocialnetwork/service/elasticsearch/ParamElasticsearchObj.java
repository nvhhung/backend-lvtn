package hcmut.cse.travelsocialnetwork.service.elasticsearch;

import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author : hung.nguyen23
 * @since : 9/14/22 Wednesday
 **/
@Data
@Builder
public class ParamElasticsearchObj {
    private String query;
    private String index;
    private List<String> indexList;
    private int from;
    private int size;
    private double lat;
    private double lon;
    private float minScore;
    private String id;
    private boolean upsert;
    private JsonObject jBody;
    private String field;
    private String type;
    private Map<String, Object> queryModel;
    private String templateCfgKey;
    private String filedSearch;
    private String key;
    private String value;
    private Class clazz;
}
