package hcmut.cse.travelsocialnetwork.application.like;

import hcmut.cse.travelsocialnetwork.command.like.CommandLike;
import hcmut.cse.travelsocialnetwork.model.Like;
import hcmut.cse.travelsocialnetwork.repository.like.ILikeRepository;
import hcmut.cse.travelsocialnetwork.service.redis.PostRedis;
import hcmut.cse.travelsocialnetwork.service.redis.RankRedis;
import hcmut.cse.travelsocialnetwork.service.redis.UserRedis;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.CustomException;
import hcmut.cse.travelsocialnetwork.utils.enums.FactorialPost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author : hung.nguyen23
 * @since : 9/20/22 Tuesday
 **/
@Component
public class LikeApplication implements ILikeApplication{
    private static final Logger log = LogManager.getLogger(LikeApplication.class);
    ILikeRepository likeRepository;
    PostRedis postRedis;
    UserRedis userRedis;
    RankRedis rankRedis;

    public LikeApplication(ILikeRepository likeRepository,
                           PostRedis postRedis,
                           UserRedis userRedis,
                           RankRedis rankRedis) {
        this.likeRepository = likeRepository;
        this.postRedis = postRedis;
        this.userRedis = userRedis;
        this.rankRedis = rankRedis;
    }

    @Override
    public Optional<Like> createLike(CommandLike commandLike) throws Exception {
        var likeNew = Like.builder()
                .userId(commandLike.getUserId())
                .postId(commandLike.getPostId())
                .build();
        var likeAdd = likeRepository.add(likeNew);
        if (likeAdd.isEmpty()) {
            log.warn(String.format("%s like post %s fail", commandLike.getUserId(), commandLike.getPostId()));
            throw new CustomException(Constant.ERROR_MSG.LIKE_FAIL);
        }

        postRedis.increaseFactorial(commandLike.getPostId(), FactorialPost.LIKE);
        var pointPostNew = postRedis.increaseAndGetPoints(commandLike.getPostId(), Constant.POINTS.ONE_LIKE_POST);
        rankRedis.addLeaderBoard(Constant.LEADER_BOARD.KEY_POST, commandLike.getPostId(), pointPostNew);

        var pointUserNew = userRedis.increaseAndGetPoints(commandLike.getUserId(), Constant.POINTS.ONE_LIKE_USER);
        rankRedis.addLeaderBoard(Constant.LEADER_BOARD.KEY_USER, commandLike.getUserId(), pointUserNew);
        // todo : push notification to owner of post
        return likeAdd;
    }

    @Override
    public Optional<Boolean> unlike(CommandLike commandLike) throws Exception {
        var query = new Document("userId", commandLike.getUserId()).append("postId", commandLike.getPostId());
        var like = likeRepository.get(query);
        if (like.isEmpty()) {
            log.warn(String.format("%s not found like in post %s", commandLike.getUserId(), commandLike.getPostId()));
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUND_LIKE);
        }

        // increase like size, point of post
        postRedis.decreaseFactorial(commandLike.getPostId(), FactorialPost.LIKE);
        var pointPostNew = postRedis.decreaseAndGetPoints(commandLike.getPostId(), Constant.POINTS.ONE_LIKE_POST);
        rankRedis.addLeaderBoard(Constant.LEADER_BOARD.KEY_POST, commandLike.getPostId(), pointPostNew);

        // increase point user like
        var pointUserNew = userRedis.decreaseAndGetPoints(commandLike.getUserId(), Constant.POINTS.ONE_LIKE_USER);
        rankRedis.addLeaderBoard(Constant.LEADER_BOARD.KEY_USER, commandLike.getUserId(), pointUserNew);

        return likeRepository.delete(like.get().get_id().toString());
    }
}
