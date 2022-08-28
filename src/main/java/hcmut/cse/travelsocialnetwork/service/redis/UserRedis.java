package hcmut.cse.travelsocialnetwork.service.redis;

import hcmut.cse.travelsocialnetwork.application.user.UserApplication;
import hcmut.cse.travelsocialnetwork.model.User;
import hcmut.cse.travelsocialnetwork.repository.user.IUserRepository;
import hcmut.cse.travelsocialnetwork.repository.user.UserRepositoryImpl;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.JSONUtils;
import hcmut.cse.travelsocialnetwork.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * @author : hung.nguyen23
 * @since : 8/25/22 Thursday
 **/
@Component
public class UserRedis {
    private static final Logger log = LogManager.getLogger(UserRedis.class);
    @Autowired
    JedisMaster jedis;
    @Autowired
    IUserRepository userRepository;

    public void updateUser(String userId, User user) {
        String userRedis = jedis.get(Constant.KEY_REDIS.USER + userId);
        if (!StringUtils.isNullOrEmpty(userRedis)) {
            jedis.delete(Constant.KEY_REDIS.USER + userId);
        }
        jedis.setWithExpireAfter(Constant.KEY_REDIS.USER + userId, JSONUtils.objToJsonString(user), Constant.TIME.SECOND_OF_ONE_DAY);
    }

    public User getUser(String userId) {
        var userRedis = JSONUtils.stringToObj(jedis.get(Constant.KEY_REDIS.USER + userId), User.class);
        if (userRedis != null) {
            log.info("user cached in redis");
            return userRedis;
        }

        log.info("no have user cached in redis");
        var userDb = userRepository.getById(userId);
        if (userDb.isEmpty()) {
            log.info("user not exist");
            return null;
        }
        jedis.setWithExpireAfter(Constant.KEY_REDIS.USER + userId, JSONUtils.objToJsonString(userDb.get()), Constant.TIME.SECOND_OF_ONE_DAY);
        return userDb.get();
    }
}
