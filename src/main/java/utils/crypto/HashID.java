package utils.crypto;

import org.hashids.Hashids;

public class HashID {
    private static final String DEFAULT_SALT = "VIHAT_OMI_CRM_25062019";

    public static String toHash(Long input, int length) {
        Hashids hashids = new Hashids(DEFAULT_SALT, length);
        String hash = hashids.encode(input);
        return hash;
    }

    public static String encodeHashHex(String input) {
        Hashids hashids = new Hashids(DEFAULT_SALT);
        String hash = hashids.encodeHex(input);
        return hash;
    }

    public static String decodeHashHex(String hash) {
        Hashids hashids = new Hashids(DEFAULT_SALT);
        String output = hashids.decodeHex(hash);
        return output;
    }
}
