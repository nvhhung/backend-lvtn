package utils.crypto;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA512 hashing sample with plain Java. Uses a salt, configures the number of iterations and calculates the hash
 * value.
 * <p/>
 * Uses Google Guava to hex the hash in a readable format.
 *
 * @author Dominik Schadow
 */
public class SHA512 {

    public static String valueOf(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");

        // digest() method is called
        // to calculate message digest of the input data
        // returned as array of byte
        byte[] messageDigest = md.digest(input.getBytes());

        // Convert byte array into signum representation
        BigInteger no = new BigInteger(1, messageDigest);

        // Convert message digest into hex value
        String hashtext = no.toString(16);

        // Add preceding 0s to make it 32 bit
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        // return the HashText
        return hashtext;
    }

}