package utils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class JSONUtils<T> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T toObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    public static <T> String toJSON(T obj) {
        return JSON.toJSONString(obj);
    }

    public static Map objectToMap(Object obj) {
        return objectMapper.convertValue(obj, Map.class);
    }

    public static <T> T jsonToObject(String JSON, Class<T> clazz) throws IOException {
        return objectMapper.readValue(JSON, clazz);
    }

    public static <T> String objectToJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }


}
