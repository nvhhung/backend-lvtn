package hcmut.cse.travelsocialnetwork.application.rank;

import hcmut.cse.travelsocialnetwork.command.rank.CommandRank;
import hcmut.cse.travelsocialnetwork.model.Rank;

import java.util.List;

/**
 * @author : hung.nguyen23
 * @since : 9/22/22 Thursday
 **/
public interface IRankApplication {
    Object getLeaderBoardUser(CommandRank commandRank);
    Object getLeaderBoardPost(CommandRank commandRank);
    Rank getRankUser(CommandRank commandRank);
    Rank getRankPost(CommandRank commandRank);
}
