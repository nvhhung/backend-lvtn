package hcmut.cse.travelsocialnetwork.application.follow;

import hcmut.cse.travelsocialnetwork.application.notification.INotificationApplication;
import hcmut.cse.travelsocialnetwork.command.follow.CommandFollow;
import hcmut.cse.travelsocialnetwork.command.notification.CommandNotification;
import hcmut.cse.travelsocialnetwork.model.Follow;
import hcmut.cse.travelsocialnetwork.repository.follow.IFollowRepository;
import hcmut.cse.travelsocialnetwork.service.redis.UserRedis;
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
    INotificationApplication notificationApplication;
    UserRedis userRedis;
    public FollowApplication(IFollowRepository followRepository,
                             INotificationApplication notificationApplication,
                             UserRedis userRedis) {
        this.followRepository = followRepository;
        this.notificationApplication = notificationApplication;
        this.userRedis = userRedis;
    }

    @Override
    public Optional<Follow> followUser(CommandFollow commandFollow) throws Exception {
        var query = new Document(Constant.FIELD_QUERY.USER_ID, commandFollow.getUserId())
                .append(Constant.FIELD_QUERY.USER_ID_TARGET, commandFollow.getUserIdTarget());
        var followOptional = followRepository.get(query);
        if (followOptional.isPresent()) {
            log.warn("follow is exist");
            throw new CustomException(Constant.ERROR_MSG.FOLLOW_IS_EXIST);
        }

        var follow = Follow.builder()
                .userId(commandFollow.getUserId())
                .userIdTarget(commandFollow.getUserIdTarget())
                .build();
        var followAdd = followRepository.add(follow);
        if (followAdd.isEmpty()) {
            log.warn(String.format("%s follow user fail",commandFollow.getUserId()));
            throw new CustomException(Constant.ERROR_MSG.FOLLOW_FAIL);
        }
        log.info(String.format("%s user follow user %s success", commandFollow.getUserId(), commandFollow.getUserIdTarget()));
        // todo : create notification to user target
        var userTrigger = userRedis.getUser(commandFollow.getUserId());
        var commandNotification = CommandNotification.builder()
                .userId(commandFollow.getUserIdTarget())
                .userIdTrigger(commandFollow.getUserId())
                .isRead(false)
                .objectId(followAdd.get().get_id().toString())
                .type(Constant.NOTIFICATION.FOLLOW)
                .title(Constant.NOTIFICATION.TITLE_FOLLOW)
                .content(String.format(Constant.NOTIFICATION.CONTENT_FOLLOW, userTrigger.getFullName()))
                .build();
        notificationApplication.createNotification(commandNotification);
        return followAdd;
    }

    @Override
    public Optional<Boolean> unFollowUser(CommandFollow commandFollow) throws Exception {
        var query = new Document(Constant.FIELD_QUERY.USER_ID, commandFollow.getUserId())
                .append(Constant.FIELD_QUERY.USER_ID_TARGET, commandFollow.getUserIdTarget());
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
        var query = new Document(Constant.FIELD_QUERY.USER_ID_TARGET, commandFollow.getUserId());
        var sort = new Document(Constant.FIELD_QUERY.LAST_UPDATE_TIME, -1);
        var followList = followRepository.search(query, sort, commandFollow.getPage(), commandFollow.getSize());
        if (followList.isEmpty()) {
            log.info(String.format("%s no have follower", commandFollow.getUserId()));
            return Optional.of(new ArrayList<>());
        }
        return followList;
    }

    @Override
    public Optional<List<Follow>> getFollowUser(CommandFollow commandFollow) throws Exception {
        var query = new Document(Constant.FIELD_QUERY.USER_ID, commandFollow.getUserId());
        var sort = new Document(Constant.FIELD_QUERY.LAST_UPDATE_TIME, -1);
        var followList = followRepository.search(query, sort, commandFollow.getPage(), commandFollow.getSize());
        if (followList.isEmpty()) {
            log.info(String.format("%s no have follow user", commandFollow.getUserId()));
            return Optional.of(new ArrayList<>());
        }
        return followList;
    }
}
