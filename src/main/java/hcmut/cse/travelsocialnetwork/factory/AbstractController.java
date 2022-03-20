package hcmut.cse.travelsocialnetwork.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class AbstractController {
    @Autowired
    protected ObjectMapper objectMapper;

    protected String outputJson(int code, Object entityDTO) {
        return this.refactorOutput(code, null, null, entityDTO);
    }
    private String refactorOutput(int code, String message, String soft_key, Object entityDTO) {
        Map<String, Object> result = new HashMap<>();
        result.put("status_code", code);
        if (StringUtils.isNotBlank(message)) {
            result.put("message", message);
        }

        result.put("key_enabled", false);
        result.put("payload", entityDTO);
        try {
            return objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
