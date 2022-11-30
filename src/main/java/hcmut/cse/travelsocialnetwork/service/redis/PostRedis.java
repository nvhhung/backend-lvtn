package hcmut.cse.travelsocialnetwork.service.redis;

import hcmut.cse.travelsocialnetwork.application.media.IMediaApplication;
import hcmut.cse.travelsocialnetwork.command.media.CommandMedia;
import hcmut.cse.travelsocialnetwork.model.Post;
import hcmut.cse.travelsocialnetwork.model.User;
import hcmut.cse.travelsocialnetwork.repository.post.IPostRepository;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.JSONUtils;
import hcmut.cse.travelsocialnetwork.utils.StringUtils;
import hcmut.cse.travelsocialnetwork.utils.enums.FactorialPost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @author : hung.nguyen23
 * @since : 9/19/22 Monday
 **/
@Component
public class PostRedis {
    private static final Logger log = LogManager.getLogger(PostRedis.class);
    JedisMaster jedis;
    IPostRepository postRepository;
    IMediaApplication mediaApplication;

    public PostRedis(JedisMaster jedis,
                     IPostRepository postRepository,
                     IMediaApplication mediaApplication) {
        this.jedis = jedis;
        this.postRepository = postRepository;
        this.mediaApplication = mediaApplication;
    }

    public Post getPost(String postId) {
        var strPostCache = jedis.get(Constant.KEY_REDIS.POST + postId);
        if (!StringUtils.isNullOrEmpty(strPostCache)) {
            log.info("post cached in redis");
            return JSONUtils.stringToObj(strPostCache, Post.class);
        }

        log.info("no have post cached in redis");
        var postDb = postRepository.getById(postId);
        if (postDb.isEmpty()) {
            log.warn(String.format("%s is not exist", postId));
            return null;
        }
        postDb.ifPresent(post -> post.setMediaList(mediaApplication.loadByPostId(CommandMedia.builder()
                .postId(post.get_id().toString())
                .page(1)
                .size(1000)
                .build()).orElse(new ArrayList<>())));

        jedis.setWithExpireAfter(Constant.KEY_REDIS.POST + postId, JSONUtils.objToJsonString(postDb.get()), Constant.TIME.TTL_POST);
        return postDb.get();
    }

    public void updatePost(String postId, Post post) {
        var postRedis = jedis.get(Constant.KEY_REDIS.POST + postId);
        if (!StringUtils.isNullOrEmpty(postRedis)) {
            jedis.delete(Constant.KEY_REDIS.POST + postId);
        }
        jedis.setWithExpireAfter(Constant.KEY_REDIS.POST + postId, JSONUtils.objToJsonString(post), Constant.TIME.TTL_POST);
    }

    public void updatePostRedisDB(String postId, Post post) {
        var postClone = post.cloneFull();
        postClone.setMediaList(null);
        var postUpdate = postRepository.update(postId, postClone);
        if (postUpdate.isEmpty()) {
            log.warn("update post fail");
            return;
        }
        jedis.setWithExpireAfter(Constant.KEY_REDIS.POST + postId, JSONUtils.objToJsonString(post), Constant.TIME.TTL_POST);
    }

    public Integer increaseAndGetPoints(String postId, Integer pointAdd) {
        var post = getPost(postId);
        post.setPoint(post.getPoint() + pointAdd);
        updatePostRedisDB(postId, post);
        return post.getPoint();
    }

    public Integer updateAndGetPoints(String postId, Integer pointOld, Integer pointNew) {
        var post = getPost(postId);
        post.setPoint(post.getPoint() - pointOld + pointNew);
        updatePostRedisDB(postId, post);
        return post.getPoint();
    }

    public Integer decreaseAndGetPoints(String postId, Integer pointAdd) {
        var post = getPost(postId);
        post.setPoint(post.getPoint() - pointAdd);
        updatePostRedisDB(postId, post);
        return post.getPoint();
    }

    public void increaseFactorial(String postId, FactorialPost factorialPost) {
        var post = getPost(postId);
        switch (factorialPost) {
            case COMMENT -> post.setCommentSize(post.getCommentSize() + 1);
            case LIKE -> post.setLikeSize(post.getLikeSize() + 1);
            case RATE -> post.setRateSize(post.getRateSize() + 1);
        }
        updatePostRedisDB(postId, post);
    }

    public void decreaseFactorial(String postId, FactorialPost factorialPost) {
        var post = getPost(postId);
        switch (factorialPost) {
            case COMMENT -> post.setCommentSize(post.getCommentSize() - 1);
            case LIKE -> post.setLikeSize(post.getLikeSize() - 1);
            case RATE -> post.setRateSize(post.getRateSize() - 1);
        }
        updatePostRedisDB(postId, post);
    }

    public void deletePost(String postId) {
        jedis.delete(Constant.KEY_REDIS.POST + postId);
        postRepository.delete(postId);
    }
}
