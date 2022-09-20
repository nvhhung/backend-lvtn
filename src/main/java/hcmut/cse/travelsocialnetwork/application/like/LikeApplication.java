package hcmut.cse.travelsocialnetwork.application.like;

import hcmut.cse.travelsocialnetwork.command.like.CommandLike;
import hcmut.cse.travelsocialnetwork.model.Like;
import hcmut.cse.travelsocialnetwork.repository.like.ILikeRepository;
import hcmut.cse.travelsocialnetwork.repository.post.IPostRepository;
import hcmut.cse.travelsocialnetwork.service.redis.PostRedis;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.CustomException;
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

    public LikeApplication(ILikeRepository likeRepository,
                           PostRedis postRedis) {
        this.likeRepository = likeRepository;
        this.postRedis = postRedis;
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
        // todo : push notification to owner of post
        // increase size like of post
        var post = postRedis.getPost(commandLike.getPostId());
        post.setLikeSize(post.getLikeSize() + 1);
        postRedis.updatePostRedisDB(commandLike.getPostId(), post);

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
        var post = postRedis.getPost(commandLike.getPostId());
        post.setLikeSize(post.getLikeSize() - 1);
        postRedis.updatePostRedisDB(commandLike.getPostId(), post);
        return likeRepository.delete(like.get().get_id().toString());
    }
}
