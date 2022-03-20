package hcmut.cse.travelsocialnetwork.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.RandomStringUtils;

public class StringUtils {
    public static final String REDIS_DELIMITER = "_";
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern emailPattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    private static final String[] arrChar = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private static final Random random = new Random();
    private static final SecureRandom secureRandom = new SecureRandom();

    public StringUtils() {
    }

    public static boolean isValidEmail(String hex) {
        return isNullOrEmpty(hex) ? false : emailPattern.matcher(hex).matches();
    }

    public static boolean isNullOrEmpty(Object o) {
        if (o == null) {
            return true;
        } else if (!(o instanceof String)) {
            return false;
        } else {
            String s1 = o.toString();
            return "".equals(s1) || "null".equals(s1);
        }
    }

    public static String joinWith(String[] array, String separator) {
        if (array != null && array.length != 0) {
            if (array.length == 1) {
                return array[0];
            } else {
                StringBuilder result = new StringBuilder();
                String[] var3 = array;
                int var4 = array.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    String string = var3[var5];
                    result.append(string);
                    result.append(separator);
                }

                return result.toString();
            }
        } else {
            return "";
        }
    }

    public static String remNotNumChar(String s) {
        return isNullOrEmpty(s) ? "0" : s.replaceAll("[^\\d]", "");
    }

    public static boolean validateSQL(String str) {
        return !isNullOrEmpty(str);
    }

    public static String leftPad(String substring, String padText, int times) {
        int predictSize = substring.length() + padText.length() * times;
        StringBuilder sb = new StringBuilder(predictSize);
        sb.append(padText.repeat(Math.max(0, times)));
        sb.append(substring);
        return sb.toString();
    }

    public static String leftPad(String substring, char padChar, int times) {
        String padText = String.valueOf(padChar);
        return leftPad(substring, padText, times);
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        if (isNullOrEmpty(phoneNumber)) {
            return false;
        } else {
            String regex = "^[0-9]";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(phoneNumber);
            return matcher.find();
        }
    }

    public static String makeUniqueId() {
        return UUID.randomUUID().toString();
    }

    public static String genRandomStringWithLength(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public static String removeAccent(String str) {
        if (str != null && !str.isEmpty()) {
            String[] signs = new String[]{"aAeEoOuUiIdDyY", "áàạảãâấầậẩẫăắằặẳẵ", "ÁÀẠẢÃÂẤẦẬẨẪĂẮẰẶẲẴ", "éèẹẻẽêếềệểễ", "ÉÈẸẺẼÊẾỀỆỂỄ", "óòọỏõôốồộổỗơớờợởỡ", "ÓÒỌỎÕÔỐỒỘỔỖƠỚỜỢỞỠ", "úùụủũưứừựửữ", "ÚÙỤỦŨƯỨỪỰỬỮ", "íìịỉĩ", "ÍÌỊỈĨ", "đ", "Đ", "ýỳỵỷỹ", "ÝỲỴỶỸ"};

            for(int i = 1; i < signs.length; ++i) {
                for(int j = 0; j < signs[i].length(); ++j) {
                    str = str.replace(signs[i].charAt(j), signs[0].charAt(i - 1));
                }
            }

            return str;
        } else {
            return "";
        }
    }

    public static String normalize(String s) {
        return s == null ? "" : removeAccent(s).replaceAll("\\s{2,}", " ").replace(":", "").replaceAll("[́|́|̀|̀|̉|̉|̃|̃|̣|̣|̆|̆|̆́|̆́|̆̀|̆̀|̆̉|̆̉|̆̃|̆̃|̣̆|̣̆|̂|̂|̂́|̂́|̂̀|̂̀|̂̉|̂̉|̂̃|̂̃|̣̂|̣̂|́|́|̀|̀|̉|̉|̃|̃|̣|̣|̂|̂|̂́|̂́|̂̀|̂̀|̂̉|̂̉|̂̃|̂̃|̣̂|̣̂|́|́|̀|̀|̉|̉|̃|̃|̣|̣|́|́|̀|̀|̉|̉|̃|̃|̣|̣|̛|̛|̛́|̛́|̛̀|̛̀|̛̉|̛̉|̛̃|̛̃|̛̣|̛̣|̂|̂|̂́|̂́|̂̀|̂̀|̂̉|̂̉|̂̃|̂̃|̣̂|̣̂|́|́|̀|̀|̉|̉|̃|̃|̣|̣|̛|̛|̛́|̛́|̛̀|̛̀|̛̉|̛̉|̛̃|̛̃|̛̣|̛̣|́|́|̀|̀|̉|̉|̃|̃|̣|̣]", "").toLowerCase().trim();
    }

    public static String stripNonValidXMLCharacters(String in) {
        StringBuffer out = new StringBuffer();
        if (in != null && !"".equals(in)) {
            for(int i = 0; i < in.length(); ++i) {
                char current = in.charAt(i);
                if (current == '\t' || current == '\n' || current == '\r' || current >= ' ' && current <= '\ud7ff' || current >= '\ue000' && current <= '�') {
                    out.append(current);
                }
            }

            return out.toString();
        } else {
            return "";
        }
    }

    public static boolean checkPersonalId(String cmnd) {
        boolean isCMND = false;
        if (cmnd.length() == 9 || cmnd.length() == 12) {
            String regex = "^[0-9]{9,12}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(cmnd);
            if (matcher.matches()) {
                isCMND = true;
            }
        }

        return isCMND;
    }

    public static String genOtp() {
        StringBuilder str = new StringBuilder();
        int total = 0;

        int format;
        int pos;
        for(format = 0; format < 5; ++format) {
            pos = secureRandom.nextInt(arrChar.length);
            str.append(arrChar[pos]);
            total += pos;
        }

        format = total % 2;
        pos = format == 0 ? random.nextInt(5) : random.nextInt(4);
        str.insert(pos + format + 1, str.charAt(pos));
        return str.toString();
    }

    public static String keywordCompleted(String searchValue) {
        return org.apache.commons.lang3.StringUtils.isNotEmpty(searchValue) ? removeAccent(searchValue.trim()).replaceAll("[́|́|̀|̀|̉|̉|̃|̃|̣|̣|̆|̆|̆́|̆́|̆̀|̆̀|̆̉|̆̉|̆̃|̆̃|̣̆|̣̆|̂|̂|̂́|̂́|̂̀|̂̀|̂̉|̂̉|̂̃|̂̃|̣̂|̣̂|́|́|̀|̀|̉|̉|̃|̃|̣|̣|̂|̂|̂́|̂́|̂̀|̂̀|̂̉|̂̉|̂̃|̂̃|̣̂|̣̂|́|́|̀|̀|̉|̉|̃|̃|̣|̣|́|́|̀|̀|̉|̉|̃|̃|̣|̣|̛|̛|̛́|̛́|̛̀|̛̀|̛̉|̛̉|̛̃|̛̃|̛̣|̛̣|̂|̂|̂́|̂́|̂̀|̂̀|̂̉|̂̉|̂̃|̂̃|̣̂|̣̂|́|́|̀|̀|̉|̉|̃|̃|̣|̣|̛|̛|̛́|̛́|̛̀|̛̀|̛̉|̛̉|̛̃|̛̃|̛̣|̛̣|́|́|̀|̀|̉|̉|̃|̃|̣|̣]", "").toLowerCase().replaceAll("\\s+", " ") : "";
    }

    public static boolean equals(String s1, String s2) {
        if ((s1 != null || !"".equals(s2)) && (!"".equals(s1) || s2 != null)) {
            boolean b1 = isNullOrEmpty(s1);
            boolean b2 = isNullOrEmpty(s2);
            return b1 == b2 && (b1 || s1.equals(s2));
        } else {
            return false;
        }
    }

    public static String removeLastComma(String str) {
        return str.endsWith(",") ? str.substring(0, str.length() - 1) : str;
    }

    public static List<String> convertStringToList(String s, String regex) {
        if (isNullOrEmpty(s)) {
            return new ArrayList();
        } else {
            String[] array = s.split(regex);
            return Arrays.asList(array);
        }
    }

    public static String maskString(String input, String mask, int length) {
        StringBuilder stringBuilder = new StringBuilder(input);

        for(int i = 0; i < length; ++i) {
            stringBuilder.replace(i, i + 1, mask);
        }

        return stringBuilder.toString();
    }

    public static String toUpperCaseAndTrim(String input) {
        return input == null ? null : input.toUpperCase().trim();
    }
}