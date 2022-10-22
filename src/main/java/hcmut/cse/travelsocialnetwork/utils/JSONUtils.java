package hcmut.cse.travelsocialnetwork.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JSONUtils {
    private static final Logger log = LogManager.getLogger(JSONUtils.class);
    private static ObjectMapper mapper = new ObjectMapper();
    private static ObjectMapper mapperThin;
    private static ObjectMapper oracleMapper;
    private static ObjectMapper oracleMapperSnakecase;
    private static ObjectMapper oracleNoTimezoneMapper;
    private static Matcher matcher;
    private static Pattern p;
    private static final String GET_VALUE_OF_FIELD_JSON = "\"(${field})\":\"((\\\\\"|[^\"])*)\"";

    public JSONUtils() {
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static <T> T jsonToObj(String object, Class<T> type) {
        return jsonToObj(object, type, false);
    }

    public static <T> T jsonToObj(JsonObject object, Class<T> type) {
        return jsonToObj(object, type, false);
    }

    public static <T> T jsonToObj(String object, Class<T> type, boolean isDisableStackTrace) {
        try {
            return mapper.readValue(object, type);
        } catch (Exception var4) {
            if (isDisableStackTrace) {
                log.error("error jsonToObj: " + var4.getMessage());
            } else {
                log.error("error jsonToObj:" + object, var4);
            }

            return null;
        }
    }

    public static <T> T jsonToObj(JsonObject object, Class<T> type, boolean isDisableStackTrace) {
        try {
            return mapper.readValue(object.encode(), type);
        } catch (Exception var4) {
            if (isDisableStackTrace) {
                log.error("error jsonToObj: " + var4.getMessage());
            } else {
                log.error("error jsonToObj:" + object, var4);
            }

            return null;
        }
    }

    public static <T> T stringToObj(String str, Class<T> type) {
        return stringToObj(str, type, false);
    }

    public static <T> T stringToObj(String object, Class<T> type, boolean isDisableStackTrace) {
        try {
            return mapper.readValue(object, type);
        } catch (Exception var4) {
            if (isDisableStackTrace) {
                log.error("error jsonToObj: " + var4.getMessage());
            } else {
                log.error("error jsonToObj:" + object, var4);
            }

            return null;
        }
    }

    public static <T> T jsonToObjForOracle(JsonObject object, Class<T> type) {
        try {
            return oracleMapper.readValue(object.encode(), type);
        } catch (Exception var3) {
            log.error("error jsonToObjForOracle:" + object, var3);
            return null;
        }
    }

    public static <T> T jsonToObjForOracle(String objectString, Class<T> type) {
        try {
            return oracleMapper.readValue(objectString, type);
        } catch (Exception var3) {
            log.error("error jsonToObjForOracle:" + objectString, var3);
            return null;
        }
    }

    public static <T> List<T> jsonToObjListForOracle(List<JsonObject> objectList, Class<T> type) {
        List<T> result = new ArrayList();
        if (objectList != null && objectList.size() > 0) {
            try {
                Iterator var3 = objectList.iterator();

                while(var3.hasNext()) {
                    JsonObject object = (JsonObject)var3.next();
                    result.add(oracleMapper.readValue(object.encode(), type));
                }

                return result;
            } catch (Exception var5) {
                log.error("error jsonToObjListForOracle:" + objectList, var5);
                return null;
            }
        } else {
            return result;
        }
    }

    public static <T> List<T> jsonToObjListForOracleUnderScore(List<JsonObject> objectList, Class<T> type) {
        List<T> result = new ArrayList();
        if (objectList != null && objectList.size() > 0) {
            try {
                Iterator var3 = objectList.iterator();

                while(var3.hasNext()) {
                    JsonObject object = (JsonObject)var3.next();
                    result.add(oracleMapperSnakecase.readValue(object.encode(), type));
                }
            } catch (Exception var5) {
                log.error("error jsonToObjListForOracleUnderScore:" + objectList, var5);
            }

            return result;
        } else {
            return result;
        }
    }

    public static String objToJsonString(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (Exception var2) {
            log.error("error objToString: " + var2.getMessage(), var2);
            return "";
        }
    }

    public static <T> Set<T> jsonToObjSetForOracleUnderScore(List<JsonObject> objectList, Class<T> type) {
        Set<T> result = new HashSet();
        if (objectList != null && objectList.size() > 0) {
            try {
                Iterator var3 = objectList.iterator();

                while(var3.hasNext()) {
                    JsonObject object = (JsonObject)var3.next();
                    result.add(oracleMapperSnakecase.readValue(object.encode(), type));
                }
            } catch (Exception var5) {
                log.error("error jsonToObjListForOracleUnderScore:" + objectList, var5);
            }

            return result;
        } else {
            return result;
        }
    }

    public static String convertToString(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (Exception var2) {
            log.error("error objToString: " + var2.getMessage(), var2);
            return "";
        }
    }

    public static String objToString(Object o) {
        try {
            JsonObject j = new JsonObject(mapper.writeValueAsString(o));

            try {
                j.remove("fullExtra");
            } catch (Exception var3) {
                log.error("error objToString: " + var3.getMessage(), var3);
            }

            return j.encode();
        } catch (Exception var4) {
            log.error("error objToString: " + var4.getMessage(), var4);
            return null;
        }
    }

    public static JsonObject objToJsonObj(Object o) {
        try {
            return new JsonObject(mapper.writeValueAsString(o));
        } catch (Exception var2) {
            log.error("error objToJsonObj: " + var2.getMessage(), var2);
            return null;
        }
    }

    public static String objToStringThin(Object o) {
        try {
            return mapperThin.writeValueAsString(o);
        } catch (Exception var2) {
            log.error("error objToThinJsonObj: " + var2.getMessage(), var2);
            return null;
        }
    }

    public static Map<String, Object> objToMap(Object obj) {
        JsonObject jo = objToJsonObj(obj);
        return jo != null ? jo.getMap() : null;
    }

    public static Map<String, Object> objToMapThin(Object obj) {
        try {
            JsonObject jo = new JsonObject(objToStringThin(obj));
            return jo.getMap();
        } catch (Exception var2) {
            return null;
        }
    }

    public static <T> T mapToObj(Map<String, String> map, Class<T> type) {
        return mapper.convertValue(map, type);
    }

    public static boolean isJSON(String source) {
        try {
            new JsonObject(source);
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static Optional<JsonObject> getJSON(String source) {
        try {
            JsonObject json = new JsonObject(source);
            return Optional.of(json);
        } catch (Exception var2) {
            log.warn("not a valid json:" + source);
            return Optional.empty();
        }
    }

    public static boolean isJSONArray(String source) {
        try {
            new JsonArray(source);
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static Optional<JsonArray> getJSONArray(String source) {
        try {
            JsonArray array = new JsonArray(source);
            return Optional.of(array);
        } catch (Exception var2) {
            log.warn("not a valid json array:" + source);
            return Optional.empty();
        }
    }

    public static boolean isValidJsonObjectAndData(String data) {
        boolean isValid = isJSON(data);
        if (isValid) {
            return !(new JsonObject(data)).isEmpty();
        } else {
            return false;
        }
    }

    public static boolean isValidJsonArrayAndData(String data) {
        boolean isValid = isValidJsonArray(data);
        if (isValid) {
            return !(new JsonArray(data)).isEmpty();
        } else {
            return false;
        }
    }

    public static JsonObject objToJsonObjUpper(Object o) {
        try {
            return new JsonObject(oracleMapper.writeValueAsString(o));
        } catch (Exception var2) {
            log.error("error objToJsonObj: " + var2.getMessage(), var2);
            return null;
        }
    }

    public static Map<String, Object> objToMapUpper(Object obj) {
        JsonObject jo = objToJsonObjUpper(obj);
        return jo != null ? jo.getMap() : null;
    }

    public static String getValueofField(String json, String field) {
        String regex = "\"(${field})\":\"((\\\\\"|[^\"])*)\"".replace("${field}", field);
        p = Pattern.compile(regex, 2);
        matcher = p.matcher(json);
        return matcher.find() ? matcher.group(2) : "";
    }

    public static List<String> joArrayToListString(JsonArray jsonArray) {
        return (List)jsonArray.stream().map(Object::toString).collect(Collectors.toList());
    }

    public static JsonObject getSimpleJson(JsonObject complexJsonObject) {
        JsonObject joSimple = new JsonObject();
        parseComplexJsonObjectToSimpleJsonObject(joSimple, complexJsonObject);
        return joSimple;
    }

    public static JsonObject getSimpleJson(Object complexObject) {
        JsonObject joSimple = new JsonObject();
        parseComplexJsonObjectToSimpleJsonObject(joSimple, objToJsonObj(complexObject));
        return joSimple;
    }

    private static void parseComplexJsonObjectToSimpleJsonObject(JsonObject joSimple, JsonObject complexJsonObject) {
        if (complexJsonObject != null) {
            Set<String> setKeys = complexJsonObject.fieldNames();
            Iterator var3 = setKeys.iterator();

            while(true) {
                while(var3.hasNext()) {
                    String key = (String)var3.next();
                    Object value = complexJsonObject.getValue(key);
                    if (value != null && value.toString().startsWith("{") && value.toString().endsWith("}")) {
                        parseComplexJsonObjectToSimpleJsonObject(joSimple, new JsonObject(value.toString().trim()));
                    } else {
                        joSimple.put(key, value);
                    }
                }

                return;
            }
        }
    }

    private static String getPrefixKey(JsonObject complexJsonObject) {
        String[] arrPathClass = complexJsonObject.getString("_class", "").split(Pattern.quote("."));
        complexJsonObject.remove("_class");
        return StringUtils.isNullOrEmpty(arrPathClass[arrPathClass.length - 1]) ? arrPathClass[arrPathClass.length - 1] : arrPathClass[arrPathClass.length - 1] + ".";
    }

    public static JsonObject parseJson(Object obj, JsonObject def) {
        if (obj == null) {
            return def;
        } else {
            try {
                return new JsonObject(obj.toString());
            } catch (Exception var3) {
                return def;
            }
        }
    }

    public static <T> List<T> jsonToObjListForOracleGMT0Timezone(List<JsonObject> objectList, Class<T> type) {
        List<T> result = new ArrayList();
        if (objectList == null) {
            return result;
        } else {
            try {
                Iterator var3 = objectList.iterator();

                while(var3.hasNext()) {
                    JsonObject object = (JsonObject)var3.next();
                    result.add(oracleNoTimezoneMapper.readValue(object.encode(), type));
                }

                return result;
            } catch (Exception var5) {
                log.error("error jsonToObjListForOracleGMT0Timezone:" + objectList, var5);
                return null;
            }
        }
    }

    public static JsonObject formatToSimpleJson(JsonObject complexJsonObject) {
        JsonObject joSimple = new JsonObject();
        parseComplexJsonObject(joSimple, complexJsonObject);
        return joSimple;
    }

    public static JsonObject parseComplexJsonObject(JsonObject joSimple, JsonObject complexJsonObject) {
        Set<String> setKeys = complexJsonObject.fieldNames();
        Iterator var3 = setKeys.iterator();

        while(true) {
            while(var3.hasNext()) {
                String key = (String)var3.next();
                Object value = complexJsonObject.getValue(key);
                if (value != null && value.toString().startsWith("{") && value.toString().endsWith("}")) {
                    parseComplexJsonObject(joSimple, new JsonObject(value.toString().trim()));
                } else {
                    joSimple.put(key, value);
                }
            }

            return joSimple;
        }
    }

    public static boolean isValidJson(String data) {
        return data.startsWith("{") && data.endsWith("}");
    }

    public static boolean isValidJsonArray(String data) {
        return data.startsWith("[") && data.endsWith("]");
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
