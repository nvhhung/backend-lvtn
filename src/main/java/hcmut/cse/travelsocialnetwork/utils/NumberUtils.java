package hcmut.cse.travelsocialnetwork.utils;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Formatter;
import java.util.Locale;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NumberUtils {
    private static final Logger LOGGER = LogManager.getLogger(NumberUtils.class);
    public static final char ZERO = '0';
    private static final Logger log = LogManager.getLogger(NumberUtils.class);
    private static final Random rd = new Random();
    private static final String SEP_DEG = "°";
    private static final String SEP_MIN = "'";
    private static final String SEP_SEC = "\"";
    private static final String SEP_COMMA = ",";
    private static final String REPLACED = ";";
    private static final int[] POW = new int[]{1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};
    private static final String NUMBER = "-?\\d+(\\.\\d+)?";
    private static NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private static DecimalFormat df;

    private NumberUtils() {
    }

    public static long stringToUNumber(String number) {
        return number != null && !"".equalsIgnoreCase(number) ? stringToUNumber(number, number.length()) : 0L;
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private static long stringToUNumber(String number, int length) {
        long result = -1L;
        if (length < 20) {
            result = 0L;

            for(int i = length - 1; i >= 0; --i) {
                int c = number.charAt(i) - 48;
                if (0 > c || c > 9) {
                    result = -1L;
                    break;
                }

                result += (long)(c * POW[length - (i + 1)]);
            }
        }

        return result;
    }

    public static int strToInt(String input) {
        if (input == null) {
            return 0;
        } else {
            input = input.trim();

            try {
                return Integer.parseInt(input);
            } catch (Exception var2) {
                log.warn("exception strToInt", var2);
                return 0;
            }
        }
    }

    public static String formatCurrency(double number) {
        return df.format(number).replace(" ", "");
    }

    public static String formatPoint(double number) {
        return NumberFormat.getInstance(Locale.GERMAN).format(number);
    }

    public static String getHMACSHA1WithoutKey(String text) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            byte[] result = mDigest.digest(text.getBytes());
            return toHexString(result);
        } catch (Exception var3) {
            log.warn("exception getHMACSHA1WithoutKey", var3);
            return "";
        }
    }

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();

        String var8;
        try {
            byte[] var2 = bytes;
            int var3 = bytes.length;
            int var4 = 0;

            while(true) {
                if (var4 >= var3) {
                    var8 = formatter.toString();
                    break;
                }

                byte b = var2[var4];
                formatter.format("%02x", b);
                ++var4;
            }
        } catch (Throwable var7) {
            try {
                formatter.close();
            } catch (Throwable var6) {
                var7.addSuppressed(var6);
            }

            throw var7;
        }

        formatter.close();
        return var8;
    }

    public static double convertDegreeToDouble(String src) {
        if (src != null && !src.equalsIgnoreCase("null")) {
            double r;
            try {
                r = Double.valueOf(src);
            } catch (Exception var9) {
                log.warn("exception convertDegreeToDouble", var9);
                r = 0.0D;
            }

            if (r > 0.0D) {
                return r;
            } else {
                src = src.replace("°", ";");
                src = src.replace(",", ";");
                src = src.replace("'", ";");
                src = src.replace("\"", ";");
                String[] ar = src.split(";");
                String hour = ar.length >= 1 ? ar[0].trim() : "0";
                String min = ar.length >= 2 ? ar[1].trim() : "0";
                String sec = ar.length >= 3 ? ar[2].trim() : "0";

                try {
                    r = Double.valueOf(hour) + (Double.valueOf(min) * 60.0D + Double.valueOf(sec)) / 3600.0D;
                } catch (Exception var8) {
                    log.warn("exception convertDegreeToDouble", var8);
                    r = 0.0D;
                }

                return r;
            }
        } else {
            return 0.0D;
        }
    }

    public static int parseInt(String val) {
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException var2) {
            log.warn("parseInt fail for value: " + val, var2);
            LOGGER.error("parse error", var2);
            return -1;
        }
    }

    public static int getInt(Object val, int def) {
        try {
            return Integer.parseInt(val.toString());
        } catch (Exception var3) {
            return def;
        }
    }

    public static long getLong(Object val, long def) {
        try {
            return Long.parseLong(val.toString());
        } catch (Exception var4) {
            return def;
        }
    }

    public static double getDouble(Object val, double def) {
        try {
            return Double.parseDouble(val.toString());
        } catch (Exception var4) {
            return def;
        }
    }

    static {
        df = (DecimalFormat)nf;
    }
}
