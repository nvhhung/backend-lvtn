package hcmut.cse.travelsocialnetwork.utils;

/**
 * @author : hung.nguyen23
 * @since : 8/1/22 Monday
 **/
public class Constant {
    public static class FIELD {
        public static final String IS_DELETED = "isDeleted";
        public static final String ID = "_id";
        public static final String LAST_UPDATED_TIME = "lastUpdatedTime";
    }

    public static class OPERATOR_MONGODB {
        public static final String SET = "$set";
        public static final String ADD_TO_SET = "$addToSet";
    }

    public static class DB_NAME {
        public static final String TRAVEL_SOCIAL_NETWORK = "travel-social-network";
    }

    public static class COLLECTION_NAME {
        public static final String USER = "user";
        public static final String POST = "post";
        public static final String COMMENT = "comment";
        public static final String FOLLOW = "follow";
        public static final String RATE = "rate";
        public static final String LIKE = "like";
    }

    public static class KEY_CONFIG {
        public static final String DB = "application.mongodb";
        public static final String REDIS = "application.redis";
    }

    public static class AUTHENTICATION_KIND {
        public static String INTERNAL = "internal";
        public static String GOOGLE = "google";
        public static String FACEBOOK = "facebook";
    }

    public static class ERROR_MSG {
        public static String PARAM_NOT_VALID = "param not valid";
        // user
        public static String NOT_FOUND_USER = "not found user";
        public static String USER_REGISTER = "user register";
        public static String USER_REGISTER_FAIL = "register fail";
        public static String USER_BLOCKED = "user blocked";
        public static String UPDATE_USER_FAIL = "update user fail";

        // post
        public static String POST_FAIL = "post fail";
        public static String NOT_FOUND_POST = "not found post";
        public static String UPDATE_POST_FAIL = "update post fail";

        // follow
        public static String FOLLOW_FAIL = "follow user fail";
        public static String UN_FOLLOW_FAIL = "un follow user fail";
        public static String NOT_FOUND_FOLLOW = "not found follow";

        // comment
        public static String COMMENT_FAIL = "comment fail";

        // like
        public static String LIKE_FAIL = "like fail";
        public static String NOT_FOUND_LIKE = "not found like";
    }

    public static class STATUS_USER {
        public static String ACTIVE = "active";
        public static String BLOCKED = "blocked";
    }

    public static class KEY_REDIS {
        public static String USER = "user_";
        public static String POST = "post_";
    }

    public static class TIME {
        public static final int MILLISECOND_OF_FIVE_MINUTE = 5 * 60 * 1000;
        public static final int MILLISECOND_OF_ONE_HOUR = 60 * 60 * 1000;
        public static final int MILLISECOND_OF_SIX_HOUR = 6 * 60 * 60 * 1000;
        public static final int MILLISECOND_OF_ONE_DAY = 24 * 60 * 60 * 1000;
        public static final int MILLISECOND_OF_SEVEN_DAY = 7 * 24 * 60 * 60 * 1000;
        public static final int SECOND_OF_TWO_MINUTE = 2 * 60;
        public static final int SECOND_OF_THREE_MINUTE = 3 * 60;
        public static final int SECOND_OF_FIVE_MINUTE = 5 * 60;
        public static final int SECOND_OF_ONE_HOUR = 60 * 60;
        public static final int SECOND_OF_TWO_HOUR = 2 * 60 * 60;
        public static final int SECOND_OF_SIX_HOUR = 6 * 60 * 60;
        public static final int SECOND_OF_ONE_DAY = 24 * 60 * 60;
        public static final int SECOND_OF_THREE_DAY = 3 * 24 * 60 * 60;
        public static final int SECOND_OF_SEVEN_DAY = 7 * 24 * 60 * 60;
        public static final int SECOND_OF_ONE_MONTH = 30 * 24 * 60 * 60;
    }
}
