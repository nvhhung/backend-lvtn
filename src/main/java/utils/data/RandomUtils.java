package utils.data;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomUtils {
    public Integer randomNumberBetween(Integer min, Integer max) {
        double x = (Math.random() * ((max - min) + 1)) + min;
        return (int) Math.floor(x);
    }

    public String randomString(String type, int length) {
        String nums = "0123456789";
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
        String mixs = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
        String mixLows = "0123456789abcdefghiklmnopqrstuvwxyz";
        String charLows = "abcdefghiklmnopqrstuvwxyz";
        String charUppercase = "ABCDEFGHIJKLMNOPQRSTUVWXTZ";
        String randomString = "";
        Random r = new Random();

        switch (type) {
            case "num":
                for (int i = 0; i < length; i++) {
                    randomString = randomString + nums.charAt(r.nextInt(nums.length()));
                }
                break;
            case "char":
                for (int i = 0; i < length; i++) {
                    randomString = randomString + chars.charAt(r.nextInt(chars.length()));
                }
                break;
            case "char_low":
                for (int i = 0; i < length; i++) {
                    randomString = randomString + charLows.charAt(r.nextInt(charLows.length()));
                }
                break;
            case "char_uppercase":
                for (int i = 0; i < length; i++) {
                    randomString = randomString + charUppercase.charAt(r.nextInt(charUppercase.length()));
                }
                break;
            case "mix":
                for (int i = 0; i < length; i++) {
                    randomString = randomString + mixs.charAt(r.nextInt(mixs.length()));
                }
                break;
            case "mix_low":
                for (int i = 0; i < length; i++) {
                    randomString = randomString + mixLows.charAt(r.nextInt(mixLows.length()));
                }
                break;
            default:
                break;
        }
        return randomString;
    }
}
