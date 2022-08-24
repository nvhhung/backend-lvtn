package hcmut.cse.travelsocialnetwork.service.redis;

import hcmut.cse.travelsocialnetwork.model.User;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * @author : hung.nguyen23
 * @since : 8/25/22 Thursday
 **/
@Component
public class UserRedis {
    @Autowired
    JedisMaster jedis;

    public void updateUserRedis(String userId, User user) {
        String userRedis = jedis.get(Constant.KEY_REDIS.USER + userId);
        if (StringUtils.isNotEmpty(userRedis)) {
            jedis.delete(Constant.KEY_REDIS.USER + userId);
        }
        jedis.setWithExpireAfter(Constant.KEY_REDIS.USER + userId, JSONUtils.objToJsonString(user), Constant.TIME.SECOND_OF_ONE_DAY);
    }
}
