package utils.crypto;

import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Component
public class SHA256 {
    public String createSHA256Signature(String key, String data) {
        String hexHash = "";
        try {
            if ((key.length() % 2) == 1)
                key += '0';
            byte[] bytes = new byte[key.length() / 2];
            for (int i = 0; i < key.length() - 1; i += 2) {
                bytes[i / 2] = (byte) Integer.parseInt(key.substring(i, i + 2), 16);
            }

            SecretKeySpec signingKey = new SecretKeySpec(bytes, "HMACSHA256");
            Mac mac = Mac.getInstance("HMACSHA256");
            mac.init(signingKey);
            mac.update(data.getBytes("UTF-8"));
            byte[] digest = mac.doFinal();
            for (byte b : digest) {
                hexHash += String.format("%02X", b);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return hexHash;
    }
}
