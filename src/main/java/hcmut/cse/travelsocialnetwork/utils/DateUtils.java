package hcmut.cse.travelsocialnetwork.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DateUtils {
    private static final Logger log = LogManager.getLogger(DateUtils.class);
    public static final int SECOND_24_H = 86400;
    public static final int SECOND_15_M = 900;
    public static final long DAY_IN_MS = 86400000L;
    public static final long ONE_HOUR_MS = 3600000L;
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final Map<String, DateFormat> mapPattern = new HashMap();

    public DateUtils() {
    }

    public static long now() {
        return System.currentTimeMillis();
    }

    public static Calendar currentDate() {
        return Calendar.getInstance();
    }

    public static Timestamp currentTimestamp() {
        return new Timestamp(now());
    }

    public static Date parse(String format, String source) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;

        try {
            date = simpleDateFormat.parse(source);
        } catch (Exception var5) {
        }

        return date;
    }

    public static LocalDate getDateAfterNow(int dayAfter) {
        LocalDate date = LocalDate.now();
        date = date.plusDays((long)dayAfter);
        return date;
    }

    public static Instant convertToInstant(Timestamp timestamp) {
        return timestamp.toInstant();
    }

    public static LocalDate getDateBeforeNow(int dayAfter) {
        LocalDate date = LocalDate.now();
        date = date.minusDays((long)dayAfter);
        return date;
    }

    public static boolean validateTimeStamp(long currentTime, Long startTime, Long endTime) {
        if (startTime != null && currentTime < startTime) {
            return false;
        } else {
            return endTime == null || endTime <= 0L || currentTime <= endTime;
        }
    }

    public static long getBeginTimeofMonth(int beforeMonth) {
        Calendar cal = Calendar.getInstance();
        cal.add(2, -1 * beforeMonth);
        cal.set(5, Calendar.getInstance().getActualMinimum(5));
        cal.set(10, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return cal.getTime().getTime();
    }

    public static long getBeginTimeofDate(long milisec) {
        Calendar cal = Calendar.getInstance();
        if (milisec != 0L) {
            cal.setTimeInMillis(milisec);
        }

        cal.set(11, cal.getMinimum(11));
        cal.set(12, cal.getMinimum(12));
        cal.set(13, cal.getMinimum(13));
        cal.set(14, cal.getMinimum(14));
        return cal.getTime().getTime();
    }

    public static long getEndTimeofDate(long milisec) {
        Calendar cal = Calendar.getInstance();
        if (milisec != 0L) {
            cal.setTimeInMillis(milisec);
        }

        cal.set(11, cal.getMaximum(11));
        cal.set(12, cal.getMaximum(12));
        cal.set(13, cal.getMaximum(13));
        cal.set(14, cal.getMaximum(14));
        return cal.getTime().getTime();
    }

    public static String getDateFromLong(long timeSTamp) {
        Date date = new Date(timeSTamp);
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }

    public static Date convertStringToDate(String days) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            return formatter.parse(days);
        } catch (ParseException var3) {
            return new Date();
        }
    }

    public static String getDateFromLong(long timeSTamp, String format) {
        Date date = new Date(timeSTamp);
        DateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static String parseFromUnix(long timeSTamp) {
        Date date = new Date(timeSTamp);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public static String parseFromUnix(long timeSTamp, String format) {
        Date date = new Date(timeSTamp);
        DateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static int getQuarter(int month) {
        switch(month) {
            case 1:
            case 2:
            case 3:
                return 1;
            case 4:
            case 5:
            case 6:
                return 2;
            case 7:
            case 8:
            case 9:
                return 3;
            case 10:
            case 11:
            case 12:
                return 4;
            default:
                return 0;
        }
    }

    public static String convertSecondtoHHmmss(long seconds) {
        LocalTime time = LocalTime.MIN.plusSeconds(seconds);
        return formatter.format(time);
    }

    public static boolean inHour(String startHour, String endHour) {
        int start = Integer.parseInt(startHour.split(":")[0]);
        int end = Integer.parseInt(endHour.split(":")[0]);
        return Calendar.getInstance().get(11) >= start && Calendar.getInstance().get(11) <= end;
    }

    public static String getTimeForFormat(long timeSTamp, String format) {
        Date date = new Date(timeSTamp);
        DateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static String getFullDateTimeFormat(long timeSTamp) {
        Date date = new Date(timeSTamp);
        DateFormat formatterFullDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formatterFullDateTime.format(date);
    }

    public static int getMonthNow() {
        return Calendar.getInstance().get(2) + 1;
    }

    public static int getYearNow() {
        return Calendar.getInstance().get(1);
    }

    public static long getHourMs(int hour) {
        return (long)hour * 3600000L;
    }

    public static String getCurrentDate(long millis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return simpleDateFormat.format(millis);
    }

    public static int getCurrentMonth(long millis) {
        if (millis == 0L) {
            return Calendar.getInstance().get(2) + 1;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);
            return calendar.get(2) + 1;
        }
    }

    public static long getBeginOfYear() {
        LocalDate now = LocalDate.now();
        LocalDate firstDay = now.with(TemporalAdjusters.firstDayOfYear());
        return Timestamp.valueOf(firstDay.atStartOfDay()).getTime();
    }

    public static long getEndOfYear() {
        LocalDate now = LocalDate.now();
        LocalDate lastDay = now.with(TemporalAdjusters.lastDayOfYear());
        return Timestamp.valueOf(lastDay.atTime(LocalTime.MAX)).getTime();
    }

    public static long getBeginOfMonth(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.set(5, cal.getActualMinimum(5));
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        return cal.getTime().getTime();
    }

    public static long getEndOfMonth(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.set(5, cal.getActualMaximum(5));
        cal.set(11, 23);
        cal.set(12, 59);
        cal.set(13, 59);
        return cal.getTime().getTime();
    }

    public static int getBeginOfWeek(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        int firstDayOfWeek = 2;
        calendar.setFirstDayOfWeek(firstDayOfWeek);
        return calendar.get(3);
    }

    public static int getEndOfWeek(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        int firstDayOfWeek = 1;
        calendar.setFirstDayOfWeek(firstDayOfWeek);
        return calendar.get(3);
    }

    public static int getMonthByTimeLong(long millis) {
        if (millis == 0L) {
            return Calendar.getInstance().get(2) + 1;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);
            return calendar.get(2) + 1;
        }
    }

    public static SimpleDateFormat getSdfWalkingDate() {
        return new SimpleDateFormat("dd/MM/yyyy");
    }

    public static String getDateStringFromLong(long timeSTamp) {
        Date date = new Date(timeSTamp);
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(date);
    }

    public static Long getTimeFromDateString(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateString);
            return date.getTime();
        } catch (Exception var3) {
            return System.currentTimeMillis();
        }
    }

    public static Long getTimeFromDateStringByFormat(String dateString, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(dateString);
            return date.getTime();
        } catch (Exception var4) {
            return System.currentTimeMillis();
        }
    }

    public static DateFormat getSdfOracle() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static Date formatOracleTSString(String date) throws ParseException {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(date);
    }

    public static final Date getStartDate() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.with(LocalTime.MIN);
        long epochDay = startOfDay.toLocalDate().toEpochDay() * TimeUnit.DAYS.toMillis(1L);
        return new Date(epochDay);
    }

    public static Date getEndDate() {
        Date startDate = getStartDate();
        long epochDay = startDate.getTime() + TimeUnit.DAYS.toMillis(1L) - 1L;
        return new Date(epochDay);
    }

    public static String createCashinVisaUserGroup() {
        SimpleDateFormat dateFormatCashinGroup = new SimpleDateFormat("MMyyyy");
        String var10000 = dateFormatCashinGroup.format(new Date());
        return "FREE_VISA_NEW_" + var10000;
    }

    public static Date getDateByTimeLong(long timestamp) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String getDateTimeString(long time, boolean less) {
        try {
            return Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern(less ? "dd/MM" : "dd/MM/yyyy"));
        } catch (Exception var4) {
            log.error("fail when parse time : " + time);
            return "";
        }
    }

    public static Date fomartOracleTSString(String date) {
        String oracleFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatterDateTimeOracle = new SimpleDateFormat(oracleFormat);

        try {
            return formatterDateTimeOracle.parse(date);
        } catch (ParseException var4) {
            log.error("Parse fail ", var4);
            return new Date();
        }
    }

    public static Date atStartOfDay(LocalDateTime localDateTime) {
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    public static Date atEndOfDay(LocalDateTime localDateTime) {
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }

    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String formatTime(long time, String pattern) {
        return ((DateFormat)mapPattern.computeIfAbsent(pattern, (create) -> {
            return new SimpleDateFormat(pattern);
        })).format(new Date(time));
    }
}
