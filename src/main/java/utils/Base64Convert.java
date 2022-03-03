/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Base64Convert<T> {

    public static <T> Map base64ToMap(String base64, Class<T> clazz) {
        try {
            T obj = base64ToObject(base64, clazz);
            return JSONUtils.objectToMap(obj);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return new HashMap<>();
    }

    public static <T> T base64ToObject(String base64, Class<T> clazz) {
        try {
            byte[] valueDecoded = Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8));
            return JSONUtils.jsonToObject(new String(valueDecoded), clazz);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public static <T> String objectToBase64(T obj) throws IOException {
        String sda = JSONUtils.objectToJson(obj);
        return Base64.getEncoder().encodeToString(sda.getBytes(StandardCharsets.UTF_8));
    }

}
