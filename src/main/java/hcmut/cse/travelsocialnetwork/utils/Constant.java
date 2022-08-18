package hcmut.cse.travelsocialnetwork.utils;

/**
 * @author : hung.nguyen23
 * @since : 8/1/22 Monday
 **/
public class Constant {
    public static class FIELD {
        public static final String IS_DELETED = "isDeleted";
        public static final String ID = "id";
        public static final String LAST_UPDATED_TIME = "lastUpdatedTime";
    }

    public static class OPERATOR_MONGODB {
        public static final String SET = "$set";
        public static final String ADD_TO_SET = "$addToSet";
    }

    public static class DB_NAME {
        public static final String USER = "user";

    }

    public static class COLLECTION_NAME {
        public static final String USER = "user";

    }

    public static class KEY_CONFIG {
        public static final String DB = "application.mongodb";
    }

    public static class AUTHENTICATION_KIND {
        public static String INTERNAL = "internal";
        public static String GOOGLE = "google";
        public static String FACEBOOK = "facebook";
    }

    public static class ERROR_MSG {
        public static String PARAM_NOT_VALID = "param not valid";
        public static String NOT_FOUNT_USER = "not found user";
        public static String USER_REGISTER = "user register";
        public static String USER_BLOCKED = "user blocked";
    }

    public static class STATUS_USER {
        public static String ACTIVE = "active";
        public static String INACTIVE = "inactive";
        public static String BLOCKED = "blocked";
    }
}
