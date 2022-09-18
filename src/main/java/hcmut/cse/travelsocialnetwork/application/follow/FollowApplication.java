package hcmut.cse.travelsocialnetwork.application.follow;

import hcmut.cse.travelsocialnetwork.command.follow.CommandFollow;
import hcmut.cse.travelsocialnetwork.model.Follow;
import hcmut.cse.travelsocialnetwork.repository.follow.IFollowRepository;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.CustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author : hung.nguyen23
 * @since : 9/17/22 Saturday
 **/
@Component
public class FollowApplication implements IFollowApplication {
    private static final Logger log = LogManager.getLogger(FollowApplication.class);

    IFollowRepository followRepository;
    public FollowApplication(IFollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    @Override
    public Optional<Follow> followUser(CommandFollow commandFollow) throws Exception {
        var follow = Follow.builder()
                .userId(commandFollow.getUserId())
                .userIdTarget(commandFollow.getUserIdTarget())
                .build();
        var followAdd = followRepository.add(follow);
        if (followAdd.isEmpty()) {
            log.warn(String.format("%s follow user fail",commandFollow.getUserId()));
            throw new CustomException(Constant.ERROR_MSG.FOLLOW_FAIL);
        }
        // todo : create notification to user target
        return followAdd;
    }

    @Override
    public Optional<Boolean> unFollowUser(CommandFollow commandFollow) throws Exception {
        var query = new Document("userId", commandFollow.getUserId()).append("userIdTarget", commandFollow.getUserIdTarget());
        var follow = followRepository.get(query);
        if (follow.isEmpty()) {
            log.warn("not found follow");
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUND_FOLLOW);
        }
        var result = followRepository.delete(follow.get().get_id().toString());
        if (result.isEmpty()) {
            log.warn(String.format("%s unfollow fail", commandFollow.getUserId()));
            throw new CustomException(Constant.ERROR_MSG.UN_FOLLOW_FAIL);
        }
        return result;
    }

    @Override
    public Optional<List<Follow>> getFollower(CommandFollow commandFollow) throws Exception {
        var query = new Document("userIdTarget", commandFollow.getUserId());
        var followList = followRepository.search(query, new Document(), 0, 20);
        if (followList.isEmpty()) {
            log.info(String.format("%s no have follower", commandFollow.getUserId()));
            return Optional.of(new ArrayList<>());
        }
        return followList;
    }

    @Override
    public Optional<List<Follow>> getFollowUser(CommandFollow commandFollow) throws Exception {
        var query = new Document("userId", commandFollow.getUserId());
        var followList = followRepository.search(query, new Document(), 0, 20);
        if (followList.isEmpty()) {
            log.info(String.format("%s no have follow user", commandFollow.getUserId()));
            return Optional.of(new ArrayList<>());
        }
        return followList;
    }
}
