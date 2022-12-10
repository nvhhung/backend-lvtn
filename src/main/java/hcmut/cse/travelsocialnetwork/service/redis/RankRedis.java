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
    JedisMaster jedis;

    public RankRedis(JedisMaster jedis) {
        this.jedis = jedis;
    }

    public void addLeaderBoard(String mainKey, String subKey, Integer point) {
        jedis.addSortedSet(mainKey, point, subKey);
    }

    public Rank getInfoRankUser(String mainKey, String userId) {
        if (jedis.get(mainKey) == null) {
            log.info("no save leader board user");
            return null;
        }
        var position =  jedis.getIndexMember(mainKey, userId);
        if (position == null) {
            log.info(String.format("no save point &s in leader board", userId));
            return null;
        }
        var point = jedis.getScoreMember(mainKey, userId);
        return Rank.builder()
                .point(point)
                .position(position)
                .userId(userId)
                .build();
    }

    public Rank getInfoRankPost(String mainKey, String postId) {
        if (jedis.get(mainKey) == null) {
            log.info("no save leader board post");
            return null;
        }
        var position =  jedis.getIndexMember(mainKey, postId);
        if (position == null) {
            log.info(String.format("no save point &s in leader board", postId));
            return null;
        }
        var point = jedis.getScoreMember(mainKey, postId);
        return Rank.builder()
                .point(point)
                .position(position)
                .postId(postId)
                .build();
    }


    public List<Rank> getLeaderBoardUser(String mainKey, int start, int end) {
        var userIdList = jedis.getListMemberFromGreatToSmall(mainKey, start, end);
        return userIdList.stream()
                .map(userId -> Rank.builder()
                        .userId(userId)
                        .point(jedis.getScoreMember(mainKey, userId))
                        .position(jedis.getIndexMember(mainKey, userId))
                .build())
                .collect(Collectors.toList());
    }

    public List<Rank> getLeaderBoardPost(String mainKey, int start, int end) {
        var postIdList = jedis.getListMemberFromGreatToSmall(mainKey, start, end);
        return postIdList.stream()
                .map(postId -> Rank.builder()
                        .postId(postId)
                        .point(jedis.getScoreMember(mainKey, postId))
                        .position(jedis.getIndexMember(mainKey, postId))
                        .build())
                .collect(Collectors.toList());
    }

    public Long countMember(String mainKey) {
        return jedis.countMember(mainKey);
    }
}
