package hcmut.cse.travelsocialnetwork.application.rank;

import hcmut.cse.travelsocialnetwork.command.rank.CommandRank;
import hcmut.cse.travelsocialnetwork.model.Paginated;
import hcmut.cse.travelsocialnetwork.model.Rank;
import hcmut.cse.travelsocialnetwork.service.redis.RankRedis;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
    public Object getLeaderBoardUser(CommandRank commandRank) {
        var start = (commandRank.getPage() -1) * commandRank.getSize();
        var end = start + commandRank.getSize();
        log.info(String.format("get rank user from %d to %d", start, end));
        var listResult =  rankRedis.getLeaderBoardUser(Constant.LEADER_BOARD.KEY_USER, start, end);
        log.info(String.format("rank user have size %d", listResult.size()));
        return Optional.of(new Paginated<>(listResult, commandRank.getPage(), commandRank.getSize(), listResult.size()));
    }

    @Override
    public Object getLeaderBoardPost(CommandRank commandRank) {
        var start = (commandRank.getPage() - 1) * commandRank.getSize();
        var end = start + commandRank.getSize();
        log.info(String.format("get rank post from %d to %d", start, end));
        var listResult = rankRedis.getLeaderBoardPost(Constant.LEADER_BOARD.KEY_POST, start, end);
        log.info(String.format("rank user have size %d", listResult.size()));
        return Optional.of(new Paginated<>(listResult, commandRank.getPage(), commandRank.getSize(), listResult.size()));
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
