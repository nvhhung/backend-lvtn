package utils.crypto;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class DSAUtils {

    public static String creatingDigitalSignature(String base64Data) {
        try {
            //Getting the private key
            PrivateKey privateKey = getPrivateKey();

            //Creating a Signature object
            Signature sign = Signature.getInstance("SHA256withDSA");

            //Initialize the signature
            sign.initSign(privateKey);
//            byte[] bytes = inputString.getBytes("UTF-8");
            byte[] bytes = Base64.getDecoder().decode(base64Data.getBytes("UTF-8"));

            //Adding data to the signature
            sign.update(bytes);

            //Calculating the signature
            byte[] signature = sign.sign();

            String encodedBase64 = Base64.getEncoder().encodeToString(signature);
            return encodedBase64;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public static Boolean signatureVerification(String base64Data, String base64Signature) {
        try {
            byte[] data = Base64.getDecoder().decode(base64Data.getBytes("UTF-8"));
            byte[] signature = Base64.getDecoder().decode(base64Signature);

            //Getting the public key
            PublicKey publicKey = getPublicKey();

            //Creating a Signature object
            Signature sign = Signature.getInstance("SHA256withDSA");

            //Initializing the signature
            sign.initVerify(publicKey);
            sign.update(data);

            //Verifying the signature
            boolean bool = sign.verify(signature);
            return bool;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return false;
    }

    private static PrivateKey getPrivateKey() {
        try {
            // Nạp public key từ file
//            FileInputStream fis = new FileInputStream("G:/vihat/crm/priKey.bin");
//            byte[] b = new byte[fis.available()];
//            fis.read(b);
//            fis.close();

            InputStream inputStream = DSAUtils.class.getResourceAsStream("/DSApriKey.bin");
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int result = bis.read();
            while (result != -1) {
                buf.write((byte) result);
                result = bis.read();
            }
//            StandardCharsets.UTF_8.name() > JDK 7
//            return buf.toString("UTF-8");

            // Tạo private key
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(buf.toByteArray());
            KeyFactory factory = KeyFactory.getInstance("DSA");
            return factory.generatePrivate(spec);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    private static PublicKey getPublicKey() {
        try {
            // Nạp public key từ file
//            FileInputStream fis = new FileInputStream("G:/vihat/crm/pubKey.bin");
//            byte[] b = new byte[fis.available()];
//            fis.read(b);
//            fis.close();

            InputStream inputStream = DSAUtils.class.getResourceAsStream("/DSApubKey.bin");
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int result = bis.read();
            while (result != -1) {
                buf.write((byte) result);
                result = bis.read();
            }

//            StandardCharsets.UTF_8.name() > JDK 7
//            return buf.toString("UTF-8");

            // Tạo public key
            X509EncodedKeySpec spec = new X509EncodedKeySpec(buf.toByteArray());
            KeyFactory factory = KeyFactory.getInstance("DSA");

            return factory.generatePublic(spec);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
