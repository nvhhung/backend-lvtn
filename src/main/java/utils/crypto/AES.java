package utils.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AES {
    private static final String DEFAULT_KEY = "VIHATOMICRM32NHA";

    public static String encryptToBase64(String input) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        // Create key and cipher
        Key aesKey = new SecretKeySpec(DEFAULT_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        // encrypt the text
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
        // to base64 data
        String result = Base64.getEncoder().encodeToString(encrypted);

        return result;
    }

    public static String decryptFromBase64(String input) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        // byte from base64 data
        byte[] encrypted = Base64.getDecoder().decode(input.getBytes());
        // Create key and cipher
        Key aesKey = new SecretKeySpec(DEFAULT_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        // decrypt the text
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        String decrypted = new String(cipher.doFinal(encrypted));

        return decrypted;
    }

}
