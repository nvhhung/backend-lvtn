package hcmut.cse.travelsocialnetwork.application.rank;

import hcmut.cse.travelsocialnetwork.command.rank.CommandRank;
import hcmut.cse.travelsocialnetwork.model.Rank;
import hcmut.cse.travelsocialnetwork.service.redis.RankRedis;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : hung.nguyen23
 * @since : 9/22/22 Thursday
 **/
@Component
public class RankApplication implements IRankApplication{
    private static final Logger log = LogManager.getLogger(RankApplication.class);
    RankRedis rankRedis;


    public RankApplication(RankRedis rankRedis) {
        this.rankRedis = rankRedis;
    }

    @Override
    public List<Rank> getLeaderBoardUser(CommandRank commandRank) {
        var start = commandRank.getPage() * commandRank.getSize();
        var end = start + commandRank.getSize();
        log.info(String.format("get rank user from %d to %d", start, end));
        return rankRedis.getLeaderBoardUser(Constant.LEADER_BOARD.KEY_USER, start, end);
    }

    @Override
    public List<Rank> getLeaderBoardPost(CommandRank commandRank) {
        var start = commandRank.getPage() * commandRank.getSize();
        var end = start + commandRank.getSize();
        log.info(String.format("get rank post from %d to %d", start, end));
        return rankRedis.getLeaderBoardPost(Constant.LEADER_BOARD.KEY_POST, start, end);
    }

    @Override
    public Rank getRankUser(CommandRank commandRank) {
        return rankRedis.getInfoRankUser(Constant.LEADER_BOARD.KEY_USER, commandRank.getUserId());
    }

    @Override
    public Rank getRankPost(CommandRank commandRank) {
        return rankRedis.getInfoRankPost(Constant.LEADER_BOARD.KEY_POST, commandRank.getPostId());
    }
}
