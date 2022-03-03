package utils.enumeration;

public class TypeEnum {
    public static final int SEND_REAL_TIME = 1;
    public static final int SEND_MESSAGE = 2;
    public static final int SEND_MESSAGE_GROUP = 3;
    public static final int SEND_MESSAGE_SHOP = 4;
    public static final int UPDATE_CURRENT_LOCATION = 5;
    public static final int UPDATE_ORDER = 6;
    public static final int ON_PUSH_REAL_TIME = 7;

    public enum WEEKDAY {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
    }

    public enum REAL_TIME {
        SEND, MESSAGE, MESSAGE_GROUP, MESSAGE_SHOP, ORDER, CURRENT, PUSH
    }


}
