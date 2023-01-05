package hcmut.cse.travelsocialnetwork.utils;

/**
 * @author : hung.nguyen23
 * @since : 8/1/22 Monday
 **/
public class Constant {
    public static class FIELD_QUERY {
        public static final String IS_DELETED = "isDeleted";
        public static final String ID = "_id";
        public static final String LAST_UPDATE_TIME = "lastUpdateTime";
        public static final String CREATE_TIME = "createTime";
        public static final String POST_ID = "postId";
        public static final String USER_ID = "userId";
        public static final String USER_ID_TARGET = "userIdTarget";
        public static final String DESTINATION = "destination";
        public static final String TYPE = "type";
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
        public static final String MEDIA = "media";
        public static final String GLOBAL_CONFIG = "globalconfig";
        public static final String NOTIFICATION = "notification";
    }

    public static class KEY_CONFIG {
        public static final String DB = "application.mongodb";
        public static final String REDIS = "application.redis";
        public static final String ELASTIC_SEARCH = "application.elasticsearch";
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
        public static String FOLLOW_IS_EXIST = "follow is exist";

        // comment
        public static String COMMENT_FAIL = "comment fail";
        public static String NOT_FOUND_COMMENT = "not found comment";
        public static String UPDATE_COMMENT_FAIL = "update comment fail";

        // like
        public static String LIKE_FAIL = "like fail";
        public static String LIKE_EXIST = "like exist";
        public static String NOT_FOUND_LIKE = "not found like";

        // rate
        public static String RATE_FAIL = "rate fail";
        public static String NOT_FOUND_RATE = "not found rate";
        public static String RATE_IS_EXIST = "rate is exist";

        // notification
        public static String NOT_FOUND_NOTIFICATION = "not found notification";
    }

    public static class STATUS_USER {
        public static String ACTIVE = "active";
        public static String BLOCKED = "blocked";
    }

    public static class KEY_REDIS {
        public static String USER = "user_";
        public static String POST = "post_";
    }

    public static class POINTS {
        public static final int ONE_LIKE_POST = 1;
        public static final int ONE_COMMENT_POST = 2;
        public static final int ONE_LIKE_USER = 2;
        public static final int ONE_COMMENT_USER = 4;
        public static final int ONE_RATE_USER = 2;
        public static final int CREATE_POST = 20;
    }

    public static class LEADER_BOARD {
        public static final String KEY_USER = "LEADER_BOARD_POINT_USER";
        public static final String KEY_POST = "LEADER_BOARD_POINT_POST";
    }

    public static class GLOBAL_CONFIG {
        public static final String QUERY_ES_POST = "QUERY_ES_POST";
        public static final String QUERY_ES_USER = "QUERY_ES_USER";
    }

    public static class NOTIFICATION {
        public static final String LIKE = "like";
        public static final String COMMENT = "comment";
        public static final String RATE = "rate";
        public static final String FOLLOW = "follow";
        public static final String TITLE_POST = "Bạn có thông báo mới về bài đăng %s.";
        public static final String CONTENT_LIKE = "%s đã thích bài viết %s.";
        public static final String CONTENT_RATE = "%s đã đánh giá bài viết %s.";
        public static final String CONTENT_COMMENT = "%s đã bình luận bài viết %s.";
        public static final String TITLE_FOLLOW = "Bạn vừa được người khác theo dõi.";
        public static final String CONTENT_FOLLOW = "%s đã bắt đầu theo dõi bạn.";
    }

    public static class TIME {
        public static final int MILLISECOND_OF_TWO_MINUTE = 2 * 60 * 1000;
        public static final int MILLISECOND_OF_THREE_MINUTE = 3 * 60 * 1000;
        public static final int TTL_POST = 10 * 60;
        public static final int TTL_USER = 20 * 60;
        public static final long EXPIRED_TOKEN = 60 * 60 * 1000L;
        public static final int SECOND_OF_ONE_MONTH = 30 * 24 * 60 * 60;
    }
}
