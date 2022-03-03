package utils.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHash {
    private static final String DEFAULT_SALT = "VIHAT_OMI_CRM_25062019";

    private static String getSHA2Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(input.getBytes());
        byte tokenBytes[] = md.digest();
        // convert token byte to token data in hex format
        StringBuffer tokenBuf = new StringBuffer();
        for (int i = 0; i < tokenBytes.length; i++) {
            tokenBuf.append(Integer.toString((tokenBytes[i] & 0xff) + 0x100,
                    16).substring(1));
        }
        return tokenBuf.toString();
    }

    public static String getPassword(String input) throws NoSuchAlgorithmException {
        return getSHA2Hash(DEFAULT_SALT + input);
    }
}
