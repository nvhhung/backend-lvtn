package hcmut.cse.travelsocialnetwork.application.follow;

import hcmut.cse.travelsocialnetwork.command.follow.CommandFollow;
import hcmut.cse.travelsocialnetwork.model.Follow;

import java.util.List;
import java.util.Optional;

/**
 * @author : hung.nguyen23
 * @since : 9/17/22 Saturday
 **/
public interface IFollowApplication {
    Optional<Follow> followUser(CommandFollow commandFollow) throws Exception;
    Optional<Boolean> unFollowUser(CommandFollow commandFollow) throws Exception;
    Optional<List<Follow>> getFollower(CommandFollow commandFollow) throws Exception;
    Optional<List<Follow>> getFollowUser(CommandFollow commandFollow) throws Exception;
}
