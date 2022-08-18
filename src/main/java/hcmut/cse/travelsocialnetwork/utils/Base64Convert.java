package hcmut.cse.travelsocialnetwork.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author : hung.nguyen23
 * @since : 8/19/22 Friday
 **/
public class Base64Convert<T> {

    public static <T> T base64ToObject(String base64, Class<T> clazz) {
        try {
            byte[] valueDecoded = Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
            return JSONUtils.jsonToObj(new String(valueDecoded), clazz);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public static <T> String objectToBase64(T obj) throws IOException {
        String sda = JSONUtils.objToJsonString(obj);
        return Base64.getEncoder().encodeToString(sda.getBytes(StandardCharsets.UTF_8));
    }
}
