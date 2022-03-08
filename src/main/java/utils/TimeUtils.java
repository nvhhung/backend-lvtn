package utils;

import eu.dozd.mongo.annotation.Embedded;
import eu.dozd.mongo.annotation.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class TimeUtils {
    private static int ZERO = 0, MAX_HOUR = 23, MAX_MIN = 59, MAX_DATE = 31, MIN_DATE = 1, MAX_MONTH = 11;
    private static String PATTERN = "dd MMM yyyy HH:mm:ss";
    private static final long MILLI_SECOND_OF_DATE = 86400000;
    public static final long MILLI_SECOND_OF_MINUTES = 60000;

    public static String formatTime(Long time) throws ParseException {
        DateFormat simple = new SimpleDateFormat(PATTERN);
        String dateAsString = simple.format(time);
        Date dateAsObj = simple.parse(dateAsString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateAsObj);
        int month = cal.getTime().getMonth() + 1;
        int year = cal.getTime().getYear() + 1900;
        String hours = cal.getTime().getHours() > 9 ? "" + cal.getTime().getHours() : "0" + cal.getTime().getHours();
        String minutes = cal.getTime().getMinutes() > 9 ? "" + cal.getTime().getMinutes() : "0" + cal.getTime().getMinutes();
        String date = cal.getTime().getDate() > 9 ? "" + cal.getTime().getDate() : "0" + cal.getTime().getDate();
        String monthS = month > 9 ? "" + month : "0" + month;
        return hours + ":" + minutes + " " + date + "/" + monthS + "/" + year;
    }


    public static Long getCurrentMillisecondByCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 8);
        int current_day = calendar.get(Calendar.DAY_OF_WEEK);
        if (current_day == 1) {
            current_day = 6;
        } else {
            current_day = current_day - 2;
        }
        calendar.add(Calendar.DATE, -current_day);
        calendar.set(Calendar.HOUR, ZERO);
        calendar.set(Calendar.MINUTE, ZERO);
        calendar.set(Calendar.SECOND, ZERO);
        calendar.set(Calendar.MILLISECOND, ZERO);
        return calendar.getTimeInMillis();
    }

    public static Long addMonths(Long time, int nbMonths) throws ParseException {
        DateFormat simple = new SimpleDateFormat(PATTERN);
        String dateAsString = simple.format(time);
        Date dateAsObj = simple.parse(dateAsString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateAsObj);
        cal.add(Calendar.MONTH, nbMonths);
        return cal.getTimeInMillis();
    }


    public static Time infoTimeByCurrent(Long time) throws ParseException {
        DateFormat simple = new SimpleDateFormat(PATTERN);
        String dateAsString = simple.format(time);
        Date dateAsObj = simple.parse(dateAsString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateAsObj);
        Time time_result = Time.builder()
                .year(cal.getTime().getYear() + 1900)
                .month(cal.getTime().getMonth() + 1)
                .day(cal.getTime().getDate())
                .hours(cal.getTime().getHours())
                .minutes(cal.getTime().getMinutes())
                .second(cal.getTime().getSeconds())
                .week(cal.get(Calendar.WEEK_OF_MONTH))
                .current_day(cal.get(Calendar.DAY_OF_WEEK))
                .max_day(cal.getActualMaximum(Calendar.DATE))
                .min_day(cal.getActualMinimum(Calendar.DATE))
                .build();
        return time_result;
    }


    public static Long getMillisLastMonthByTime(Long time) throws ParseException {
        DateFormat simple = new SimpleDateFormat(PATTERN);
        String dateAsString = simple.format(time);
        Date dateAsObj = simple.parse(dateAsString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateAsObj);
        if (cal.getTime().getMonth() == 0) {
            cal.set(cal.getTime().getYear() + 1899, MAX_MONTH, MIN_DATE, ZERO, 1, ZERO);
        } else {
            cal.set(cal.get(Calendar.YEAR), cal.getTime().getMonth() - 1, MIN_DATE, ZERO, ZERO, ZERO);
        }
        return cal.getTimeInMillis();
    }

    public static int getMaxDate() {
        int iYear = 2020;
        int iMonth = Calendar.FEBRUARY; // 1 (months begin with 0)
        int iDay = 1;

// Create a calendar object and set year and month
        Calendar mycal = new GregorianCalendar(iYear, iMonth, iDay);

// Get the number of days in that month
        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28
        return daysInMonth;
    }

    public static int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    public static Long randTime(Integer year_start, Integer year_end, Integer month_start, Integer month_end) throws ParseException {
        GregorianCalendar gc = new GregorianCalendar();
        Long currentTime = System.currentTimeMillis();
        Time time = infoTimeByCurrent(currentTime);
        int year = time.getYear();
        int month = time.getMonth();
        if (year_start == null || year_start > time.getYear()) {
            year_start = year;
            year_end = year;
        }
        if (year_end == null && year_start != null || year_end > time.getYear()) {
            year_end = year;
        }

        year = randBetween(year_start, year_end);
        gc.set(gc.YEAR, year);
        if (year >= time.getYear()) {
            if (month_start == null) {
                month_start = month;
                month_end = month;
            }
            if (month_end == null && month_start != null) {
                month_end = month;
            }
            month = randBetween(month_start, month_end);
            gc.set(gc.MONTH, month - 1);
            if (month >= time.getMonth()) {
                int dayOfMonth = randBetween(1, time.getDay());
                gc.set(gc.DAY_OF_MONTH, dayOfMonth);
            } else {// nho hon thang hien tai
                int dayOfMonth = randBetween(1, gc.getActualMaximum(gc.DAY_OF_MONTH));
                gc.set(gc.DAY_OF_MONTH, dayOfMonth);
            }
        } else {
            month = randBetween(1, gc.getActualMaximum(gc.MONTH) + 1);
            gc.set(gc.MONTH, month);
            int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_MONTH));
            gc.set(gc.DAY_OF_MONTH, dayOfYear);
        }
        final Random random = new Random();
        final int millisInDay = 24 * 60 * 60 * 1000;
        Long timeMillis = ((long) random.nextInt(millisInDay)) + gc.getTimeInMillis();
        Time timeOfDay = new Time(timeMillis);
        gc.setTime(timeOfDay);
        return gc.getTimeInMillis();
    }

    public static Long convertStringToMilliseconds(String time, String format) {
        try {
            if (StringUtils.isAnyBlank(time)) {
                return System.currentTimeMillis();
            }
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (Exception e) {

        }
        return System.currentTimeMillis();
    }

    public static Integer getMaxDayOfMonth(Long time) throws ParseException {
        Time timeResult = infoTimeByCurrent(time);
        return timeResult.getMax_day();
    }

    public static Integer getCurrentDayByTime(Long time) throws ParseException {
        Time timeResult = infoTimeByCurrent(time);
        return timeResult.getDay();
    }

    public static Long getTimeMillisMinDayOfYear(Long timeParse) {
        try {
            DateFormat simple = new SimpleDateFormat(PATTERN);
            String dateAsString = simple.format(timeParse);
            Date dateAsObj = simple.parse(dateAsString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateAsObj);
            calendar.set(calendar.get(Calendar.YEAR), ZERO, MIN_DATE, ZERO, ZERO, ZERO);
            return calendar.getTimeInMillis();
        } catch (Exception e) {
        }
        return System.currentTimeMillis();
    }

    public static Long getTimeMillisMaxDayOfYear(Long timeParse) {
        try {
            DateFormat simple = new SimpleDateFormat(PATTERN);
            String dateAsString = simple.format(timeParse);
            Date dateAsObj = simple.parse(dateAsString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateAsObj);
            calendar.set(calendar.get(Calendar.YEAR), MAX_MONTH, MAX_DATE, MAX_MIN, MAX_MIN, MAX_MIN);
            return calendar.getTimeInMillis();
        } catch (Exception e) {
        }
        return System.currentTimeMillis();
    }

    public static Long getTimeMillisMinDayOfMonth(Long timeParse) {
        try {
            DateFormat simple = new SimpleDateFormat(PATTERN);
            String dateAsString = simple.format(timeParse);
            Date dateAsObj = simple.parse(dateAsString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateAsObj);
            int min_day = calendar.getActualMinimum(Calendar.DATE);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), min_day, ZERO, ZERO, ZERO);
            return calendar.getTimeInMillis();
        } catch (Exception e) {
        }
        return System.currentTimeMillis();
    }

    public static Long getTimeMillisMaxDayOfMonth(Long timeParse) {
        try {
            DateFormat simple = new SimpleDateFormat(PATTERN);
            String dateAsString = simple.format(timeParse);
            Date dateAsObj = simple.parse(dateAsString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateAsObj);
            int max_day = calendar.getActualMaximum(Calendar.DATE);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), max_day, MAX_HOUR, MAX_MIN, MAX_MIN);
            return calendar.getTimeInMillis();
        } catch (Exception e) {
        }
        return System.currentTimeMillis();
    }

    public static Long getTimeMillisMinTimeOfDate(Long timeParse) {
        try {
            DateFormat simple = new SimpleDateFormat(PATTERN);
            String dateAsString = simple.format(timeParse);
            Date dateAsObj = simple.parse(dateAsString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateAsObj);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), ZERO, ZERO, ZERO);
            return calendar.getTimeInMillis();
        } catch (Exception e) {
        }
        return System.currentTimeMillis();
    }

    public static Long getTimeMillisMaxTimeOfDate(Long timeParse) {
        try {
            DateFormat simple = new SimpleDateFormat(PATTERN);
            String dateAsString = simple.format(timeParse);
            Date dateAsObj = simple.parse(dateAsString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateAsObj);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), MAX_HOUR, MAX_MIN, MAX_MIN);
            return calendar.getTimeInMillis();
        } catch (Exception e) {
        }
        return System.currentTimeMillis();
    }

    public static Long getTimeMillisMinTimeOfHour(Long timeParse, int hour) {
        try {
            DateFormat simple = new SimpleDateFormat(PATTERN);
            String dateAsString = simple.format(timeParse);
            Date dateAsObj = simple.parse(dateAsString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateAsObj);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hour, ZERO, ZERO);
            return calendar.getTimeInMillis();
        } catch (Exception e) {
        }
        return System.currentTimeMillis();
    }

    public static Long getTimeMillisMaxTimeOfHour(Long timeParse, int hour) {
        try {
            DateFormat simple = new SimpleDateFormat(PATTERN);
            String dateAsString = simple.format(timeParse);
            Date dateAsObj = simple.parse(dateAsString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateAsObj);
            calendar.set(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    hour, MAX_MIN, MAX_MIN);
            return calendar.getTimeInMillis();
        } catch (Exception e) {
        }
        return System.currentTimeMillis();
    }

    public static Long getTimeMillisMinTimeOfDay(Long timeParse, int day) {
        try {
            DateFormat simple = new SimpleDateFormat(PATTERN);
            String dateAsString = simple.format(timeParse);
            Date dateAsObj = simple.parse(dateAsString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateAsObj);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), day, ZERO, ZERO, ZERO);
            return calendar.getTimeInMillis();
        } catch (Exception e) {
        }
        return System.currentTimeMillis();
    }

    public static Long getTimeMillisMaxTimeOfDay(Long timeParse, int day) {
        try {
            DateFormat simple = new SimpleDateFormat(PATTERN);
            String dateAsString = simple.format(timeParse);
            Date dateAsObj = simple.parse(dateAsString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateAsObj);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), day, MAX_HOUR, MAX_MIN, MAX_MIN);
            return calendar.getTimeInMillis();
        } catch (Exception e) {
        }
        return System.currentTimeMillis();
    }

    public static Long getTimeMillisMaxTimeOfDay(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse(time);
        long millis = date.getTime();
        try {
            DateFormat simple = new SimpleDateFormat(PATTERN);
            String dateAsString = simple.format(millis);
            Date dateAsObj = simple.parse(dateAsString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateAsObj);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), MAX_HOUR, MAX_MIN, MAX_MIN);
            return calendar.getTimeInMillis();
        } catch (Exception e) {
        }
        return System.currentTimeMillis();
    }

    public static Long getTimeMillisMaxTimeOfDay(Long time) throws ParseException {
        try {
            DateFormat simple = new SimpleDateFormat(PATTERN);
            String dateAsString = simple.format(time);
            Date dateAsObj = simple.parse(dateAsString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateAsObj);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), MAX_HOUR, MAX_MIN, MAX_MIN);
            return calendar.getTimeInMillis();
        } catch (Exception e) {
        }
        return System.currentTimeMillis();
    }

    public static Long getTimeMillisMinTimeOfDay(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse(time);
        long millis = date.getTime();
        try {
            DateFormat simple = new SimpleDateFormat(PATTERN);
            String dateAsString = simple.format(millis);
            Date dateAsObj = simple.parse(dateAsString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateAsObj);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), ZERO, ZERO, ZERO);
            return calendar.getTimeInMillis();
        } catch (Exception e) {
        }
        return System.currentTimeMillis();
    }

    public static Long getTimeMillisMinTimeOfDay(Long time) throws ParseException {
        try {
            DateFormat simple = new SimpleDateFormat(PATTERN);
            String dateAsString = simple.format(time);
            Date dateAsObj = simple.parse(dateAsString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateAsObj);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), ZERO, ZERO, ZERO);
            return calendar.getTimeInMillis();
        } catch (Exception e) {
        }
        return System.currentTimeMillis();
    }

    public static Long getTimeMillisMaxTimeOfMonth(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse(time);
        long millis = date.getTime();
        try {
            DateFormat simple = new SimpleDateFormat(PATTERN);
            String dateAsString = simple.format(millis);
            Date dateAsObj = simple.parse(dateAsString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateAsObj);
            int max_day = calendar.getActualMaximum(Calendar.DATE);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), max_day, MAX_HOUR, MAX_MIN, MAX_MIN);
            return calendar.getTimeInMillis();
        } catch (Exception e) {
        }
        return System.currentTimeMillis();
    }


    public static Long getTimeMillisMinTimeOfMonth(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse(time);
        long millis = date.getTime();
        try {
            DateFormat simple = new SimpleDateFormat(PATTERN);
            String dateAsString = simple.format(millis);
            Date dateAsObj = simple.parse(dateAsString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateAsObj);
            int min_day = calendar.getActualMinimum(Calendar.DATE);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), min_day, ZERO, ZERO, ZERO);
            return calendar.getTimeInMillis();
        } catch (Exception e) {
        }
        return System.currentTimeMillis();
    }

    public static String generateName(String name) {
        try {
            Time time = infoTimeByCurrent(System.currentTimeMillis());
            String month = time.getMonth().toString();

            if (time.getMonth() < 10) {
                month = "0" + time.getMonth();
            }
            String day = time.getDay().toString();
            if (time.getDay() < 10) {
                day = "0" + time.getDay();
            }
            if (StringUtils.isNotEmpty(name)) {
                return name + "_" + day + "-" + month + "-" + time.getYear() + "_" + System.currentTimeMillis();
            }
            return day + "-" + month + "-" + time.getYear() + "_" + System.currentTimeMillis();
        } catch (Exception e) {
        }
        return null;
    }

    public static String formatTimeToString(Long millis) {
        try {
            Time time = infoTimeByCurrent(millis);
            String month = time.getMonth().toString();
            if (time.getMonth() < 10) {
                month = "0" + time.getMonth();
            }
            String day = time.getDay().toString();
            if (time.getDay() < 10) {
                day = "0" + time.getDay();
            }
            return day + "/" + month + "/" + time.getYear();
        } catch (Exception e) {
        }
        return null;
    }

    public static String formatTimeToStringNew(Long millis) {
        try {
            Time time = infoTimeByCurrent(millis);
            String month = time.getMonth().toString();
            if (time.getMonth() < 10) {
                month = "0" + time.getMonth();
            }
            String day = time.getDay().toString();
            if (time.getDay() < 10) {
                day = "0" + time.getDay();
            }
            return time.getHours() + ":" + time.getMinutes() + ":" + time.getSecond() + " - " + day + "/" + month + "/" + time.getYear();
        } catch (Exception e) {
        }
        return null;
    }

    public static long previousTimeApprox(Long time, int date) {
        Long currentTime = System.currentTimeMillis();
        if (time != null) {
            currentTime = time;
        }
        long millis = currentTime - date * MILLI_SECOND_OF_DATE;
        try {
            DateFormat simple = new SimpleDateFormat(PATTERN);
            String dateAsString = simple.format(millis);
            Date dateAsObj = simple.parse(dateAsString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateAsObj);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), ZERO, ZERO, ZERO);
            return calendar.getTimeInMillis();
        } catch (Exception e) {
        }
        return System.currentTimeMillis();
    }

    public static String convertTimeToString(Long time) throws ParseException {
        DateFormat simple = new SimpleDateFormat(PATTERN);
        String dateAsString = simple.format(time);
        Date dateAsObj = simple.parse(dateAsString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateAsObj);
        int month = cal.getTime().getMonth() + 1;
        int year = cal.getTime().getYear() + 1900;
        String date = cal.getTime().getDate() > 9 ? "" + cal.getTime().getDate() : "0" + cal.getTime().getDate();
        String monthS = month > 9 ? "" + month : "0" + month;
        return year + "-" + monthS + "-" + date;
    }

    public static String getYear(Long time) throws ParseException {
        DateFormat simple = new SimpleDateFormat(PATTERN);
        String dateAsString = simple.format(time);
        Date dateAsObj = simple.parse(dateAsString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateAsObj);
        int month = cal.getTime().getMonth() + 1;
        int year = cal.getTime().getYear() + 1900;
        return year + "";
    }

    private static boolean isLeapYear(int year) {
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                if (year % 400 == 0) return true;
                return false;
            }
            return true;
        }
        return false;
    }

    private static int getTotalDayByMonth(int year, int month) {
        if (month == 2) {
            if (isLeapYear(year)) {
                return 29;
            }
            return 28;
        }
        if (month == 4 || month == 4 || month == 9 || month == 11) {
            return 30;
        }
        return 31;
    }

    private static int currentDayToEndDayOfMonth(int year, int month, int day) {
        int totalDayOfMonth = getTotalDayByMonth(year, month);
        return totalDayOfMonth - day;
    }

    public static int minusDate(Long current, Long time) throws ParseException {
        if (current == null || time == null) {
            return -1;
        }
        Time currentTime = infoTimeByCurrent(current);
        Time infoTime = infoTimeByCurrent(time);
        if (current < time) {
            int yearCurrent = currentTime.getYear(), yearTime = infoTime.getYear();
            if (yearCurrent == yearTime) {
                if (currentTime.getMonth() == infoTime.getMonth()) {
                    return infoTime.getDay() - currentTime.getDay();
                } else {
                    int totalDay = currentDayToEndDayOfMonth(currentTime.getYear(), currentTime.getMonth(), currentTime.getDay()) + infoTime.getDay();
                    for (int month = currentTime.getMonth() + 1; month < infoTime.getMonth(); month++) {
                        totalDay += getTotalDayByMonth(currentTime.getYear(), month);
                    }
                    return totalDay;
                }
            }
            if (currentTime.getYear() < infoTime.getYear()) {
                int totalDay = currentDayToEndDayOfMonth(currentTime.getYear(), currentTime.getMonth(), currentTime.getDay());
                for (int month = currentTime.getMonth() + 1; month < 13; month++) {
                    totalDay += getTotalDayByMonth(currentTime.getYear(), month);
                }
                for (int year = currentTime.getYear() + 1; year < infoTime.getYear(); year++) {
                    for (int month = 1; month < 13; month++) {
                        totalDay += getTotalDayByMonth(year, month);
                    }
                }
                for (int month = 1; month < infoTime.getMonth(); month++) {
                    totalDay += getTotalDayByMonth(infoTime.getYear(), month);
                }
                totalDay += infoTime.getDay();
                return totalDay;
            }
        }
        return -1;
    }

    public static String formatMonthYear(Integer month, Integer year) {
        String result = year.toString() + "-";
        if (month < 10) result += "0" + month;
        else result += month;
        return result;
    }

    public static Long getTimeMillisMinTimeOfMonth(Integer month, Integer year) {
        Calendar calendar = new GregorianCalendar(year, month - 1, 1);
        return calendar.getTimeInMillis();
    }

    public static Long getTimeMillisMinTimeOfThisWeek() {
        return Timestamp.valueOf(LocalDateTime.now()
                .with(LocalTime.MIN)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))).getTime();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embedded
    @Builder
    public static class Time implements Serializable {
        private Integer year;
        private Integer month;
        private Integer day;
        private Integer hours;
        private Integer minutes;
        private Integer second;
        private Integer week;
        private Integer current_day;
        private Integer max_day;
        private Integer max_day_of_week;
        private Integer min_day;
        private Integer min_day_of_week;
    }
}
