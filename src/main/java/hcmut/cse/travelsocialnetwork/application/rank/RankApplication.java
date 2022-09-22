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
    public List<Rank> getLeaderBoard(CommandRank commandRank) {
        var start = commandRank.getPage() * commandRank.getSize();
        var end = start + commandRank.getSize();
        return rankRedis.getLeaderBoard(Constant.LEADER_BOARD.USER, start, end);
    }

    @Override
    public Rank getRank(CommandRank commandRank) {
    //        var rank = rankRedis.getIndexRank()
        return Rank.builder().build();
    }
}
