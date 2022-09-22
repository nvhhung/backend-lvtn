package hcmut.cse.travelsocialnetwork.application.rank;

import hcmut.cse.travelsocialnetwork.command.rank.CommandRank;
import hcmut.cse.travelsocialnetwork.model.Rank;

import java.util.List;

/**
 * @author : hung.nguyen23
 * @since : 9/22/22 Thursday
 **/
public interface IRankApplication {
    List<Rank> getLeaderBoard(CommandRank commandRank);
    Rank getRank(CommandRank commandRank);
}
