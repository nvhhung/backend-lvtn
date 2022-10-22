package hcmut.cse.travelsocialnetwork.factory.repo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import lombok.NonNull;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : hung.nguyen23
 * @since : 8/1/22 Monday
 **/
public class BuildQuerySet {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new SimpleModule() {
                @Override
                public void setupModule(SetupContext context) {
                    super.setupModule(context);
                    context.addBeanSerializerModifier(new Custom2BeanSerializerModifier());
                }
            });

    private static class Custom2BeanSerializerModifier extends BeanSerializerModifier {
        @Override
        public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
            return beanProperties.stream().filter(property -> property.getAnnotation(TransField.class) == null).collect(Collectors.toList());
        }
    }

    public static Document buildQuerySet(@NonNull Object object) {
        var typeRef = new TypeReference<HashMap<String, Object>>() {};
        Map<String, Object> data = objectMapper.convertValue(object, typeRef);
        Document queryItem = new Document();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (entry.getValue() != null) {
                queryItem.put(entry.getKey(), entry.getValue());
            }
        }
        Document query = new Document();
        queryItem.remove(Constant.FIELD_QUERY.ID);
        query.put(Constant.OPERATOR_MONGODB.SET, queryItem);
        return query;
    }

    public static Document buildQueryUpsert(@NonNull Object object) {
        var typeRef = new TypeReference<HashMap<String, Object>>() {};
        Map<String, Object> data = objectMapper.convertValue(object, typeRef);
        Document queryItem = new Document();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            queryItem.put(entry.getKey(), entry.getValue());
        }
        Document query = new Document();
        queryItem.remove(Constant.FIELD_QUERY.ID);
        queryItem.remove(Constant.FIELD_QUERY.IS_DELETED);
        query.put(Constant.OPERATOR_MONGODB.SET, queryItem);
        return query;
    }

    public static Map<String, Object> buildContent(@NonNull Object object) {
        TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
        };
        Map<String, Object> data = objectMapper.convertValue(object, typeRef);
        data.put(Constant.FIELD_QUERY.ID, data.get(Constant.FIELD_QUERY.ID));
        return data;
    }
}
