package hcmut.cse.travelsocialnetwork.service.redis;

import hcmut.cse.travelsocialnetwork.model.User;
import hcmut.cse.travelsocialnetwork.repository.user.IUserRepository;
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
        jedis.setWithExpireAfter(Constant.KEY_REDIS.USER + userId, JSONUtils.objToJsonString(user), Constant.TIME.TTL_USER);
    }

    public void deleteUser(String userId) {
        String userRedis = jedis.get(Constant.KEY_REDIS.USER + userId);
        if (!StringUtils.isNullOrEmpty(userRedis)) {
            jedis.delete(Constant.KEY_REDIS.USER + userId);
            jedis.deleteMember(Constant.LEADER_BOARD.KEY_USER, userId);
        }
    }

    public void updateUserRedisDB(String userId, User user) {
        var userUpdate = userRepository.update(userId, user);
        if (userUpdate.isEmpty()) {
            log.warn("update user fail");
            return;
        }
        jedis.setWithExpireAfter(Constant.KEY_REDIS.USER + userId, JSONUtils.objToJsonString(user), Constant.TIME.TTL_USER);
    }

    public User getUser(String userId) {
        var strUserCache = jedis.get(Constant.KEY_REDIS.USER + userId);
        if (!StringUtils.isNullOrEmpty(strUserCache)) {
            log.info("user cached in redis");
            return JSONUtils.stringToObj(strUserCache, User.class);
        }

        log.info("no have user cached in redis");
        var userDb = userRepository.getById(userId);
        if (userDb.isEmpty()) {
            log.warn("user not exist");
            return null;
        }
        jedis.setWithExpireAfter(Constant.KEY_REDIS.USER + userId, JSONUtils.objToJsonString(userDb.get()), Constant.TIME.TTL_USER);
        return userDb.get();
    }

    public Integer increaseAndGetPoints(String userId, Integer pointAdd) {
        var user = getUser(userId);
        user.setExperiencePoint(user.getExperiencePoint() + pointAdd);
        updateUserRedisDB(userId, user);
        return user.getExperiencePoint();
    }

    public Integer decreaseAndGetPoints(String userId, Integer pointAdd) {
        var user = getUser(userId);
        user.setExperiencePoint(user.getExperiencePoint() - pointAdd);
        updateUserRedisDB(userId, user);
        return user.getExperiencePoint();
    }
}
