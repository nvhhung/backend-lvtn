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
}
