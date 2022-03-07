package ioc.bootstrap.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import factory.provider.InetAddressProvider;
import io.vertx.ext.web.RoutingContext;
import ioc.bootstrap.configuration.ENVConfig;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import utils.crypto.AESUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Log4j2
public abstract class AbstractResource {
    private static final String IV = "F27D5C9927726BCEFE7510B1BDD3D137";
    private static final String RESPONSE_SALT = "88b1e2071884c5622dedb5ef5b2c6c9575d4109c2c2e3762";
    protected static final String CONTENT_TYPE = "content-type";
    protected static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";

    @Autowired
    protected ENVConfig applicationConfig;
    @Autowired
    protected InetAddressProvider inetAddressProvider;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected AESUtils aesUtils;

    private String refactorOutput(int code, String message, String soft_key, Object entityDTO) {
        Map<String, Object> result = new HashMap<>();
        result.put("status_code", code);
        result.put("instance_id", applicationConfig.getStringProperty("application.env", UUID.randomUUID().toString()));
        result.put("instance_name", inetAddressProvider.getInetAddress().getHostName());
        if (StringUtils.isNotBlank(message)) {
            result.put("message", message);
        }
        if (StringUtils.isNotBlank(soft_key)) {
            result.put("key_enabled", true);
            result.put("payload", this.encryptData(entityDTO, soft_key));
        } else {
            result.put("key_enabled", false);
            result.put("payload", entityDTO);
        }
        try {
            return objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String refactorOutputUpload(int number, String fileName, String url) {
        Map<String, Object> result = new HashMap<>();
        result.put("uploaded", number);
        result.put("fileName", fileName);
        result.put("url", url);
        try {
            return objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String encryptData(Object entityDTO, String soft_key) {
        try {
            return aesUtils.encrypt(RESPONSE_SALT, IV, soft_key, objectMapper.writeValueAsString(entityDTO));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    protected String outputJson(int code, Object entityDTO) {
        return this.refactorOutput(code, null, null, entityDTO);
    }

    protected String outputJson(int code, String message, Object entityDTO) {
        return this.refactorOutput(code, message, null, entityDTO);
    }

    protected String outputJson(int code, Object entityDTO, String soft_key) {
        return this.refactorOutput(code, null, soft_key, entityDTO);
    }

    protected String outputJson(int upload, String fileName, String url) {
        return this.refactorOutputUpload(upload, fileName, url);
    }

    protected String getUserId(RoutingContext routingContext) {
        return routingContext.user().principal().getString("user_id");
    }

    protected int getPage(RoutingContext routingContext) {
        int page = NumberUtils.toInt(routingContext.request().getParam("page"));
        if (page < 1) page = 1;
        return page;
    }

    protected int getSize(RoutingContext routingContext) {
        int size = NumberUtils.toInt(routingContext.request().getParam("size"));
        if (size <= 0) {
            size = 50;
        } else if (size > 50) {
            size = 50;
        }
        return size;
    }

    protected int getSizeLimit(RoutingContext routingContext, int limit) {
        int size = NumberUtils.toInt(routingContext.request().getParam("size"));
        if (limit > 100) limit = 100;
        if (size <= 0) {
            size = limit;
        } else if (size > limit) {
            size = limit;
        }
        return size;
    }

    protected void responseErr(RoutingContext routingContext, Throwable throwable) {
        log.error(throwable);
        routingContext.response()
                .setStatusCode(200)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(outputJson(-9999, throwable.getMessage(), new HashMap<>()));
    }
}
