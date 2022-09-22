package hcmut.cse.travelsocialnetwork.service.redis;

import hcmut.cse.travelsocialnetwork.model.Rank;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : hung.nguyen23
 * @since : 9/22/22 Thursday
 **/
@Component
public class RankRedis {
    private static final Logger log = LogManager.getLogger(RankRedis.class);
    private static final String KEY_USER = "LEADER_BOARD_POINT_USER_";
    JedisMaster jedis;

    public RankRedis(JedisMaster jedis) {
        this.jedis = jedis;
    }

    public void addLeaderBoard(String mainKey, String userId, Integer point) {

    }

    public int getIndexRank(String mainKey, String userId) {
        if (jedis.get(mainKey) == null) {
            return 0;
        }
        return jedis.getIndexMember(mainKey, userId);
    }

    public List<Rank> getLeaderBoard(String mainKey, int start, int end) {
        var userIdList = jedis.getListMemberFromGreatToSmall(mainKey, start, end);
        return userIdList.stream()
                .map(userId -> Rank.builder()
                        .userId(userId)
                        .point(jedis.getScoreMember(mainKey, userId))
                .build())
                .collect(Collectors.toList());
    }
}
