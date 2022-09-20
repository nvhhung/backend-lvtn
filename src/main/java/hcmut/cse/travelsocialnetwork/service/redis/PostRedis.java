package hcmut.cse.travelsocialnetwork.service.redis;

import hcmut.cse.travelsocialnetwork.model.Post;
import hcmut.cse.travelsocialnetwork.repository.post.IPostRepository;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.JSONUtils;
import hcmut.cse.travelsocialnetwork.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author : hung.nguyen23
 * @since : 9/19/22 Monday
 **/
@Component
public class PostRedis {
    private static final Logger log = LogManager.getLogger(PostRedis.class);
    JedisMaster jedis;
    IPostRepository postRepository;

    public PostRedis(JedisMaster jedis,
                     IPostRepository postRepository) {
        this.jedis = jedis;
        this.postRepository = postRepository;
    }

    public Post getPost(String postId) {
        var postCache = JSONUtils.stringToObj(jedis.get(Constant.KEY_REDIS.POST + postId), Post.class);
        if (postCache != null) {
            log.info(String.format("%s post cached in redis", postId));
            return postCache;
        }

        log.info("no have post cached in redis");
        var postDb = postRepository.getById(postId);
        if (postDb.isEmpty()) {
            log.warn(String.format("%s is not exist", postId));
            return null;
        }
        jedis.setWithExpireAfter(Constant.KEY_REDIS.POST + postId, JSONUtils.objToJsonString(postDb.get()), Constant.TIME.SECOND_OF_ONE_DAY);
        return postDb.get();
    }

    public void updatePost(String postId, Post post) {
        var postRedis = jedis.get(Constant.KEY_REDIS.POST + postId);
        if (!StringUtils.isNullOrEmpty(postRedis)) {
            jedis.delete(Constant.KEY_REDIS.POST + postId);
        }
        jedis.setWithExpireAfter(Constant.KEY_REDIS.POST + postId, JSONUtils.objToJsonString(post), Constant.TIME.SECOND_OF_ONE_DAY);
    }

    public void updatePostRedisDB(String postId, Post post) {
        var postUpdate = postRepository.update(postId, post);
        if (postUpdate.isEmpty()) {
            log.warn("update post fail");
            return;
        }
        jedis.setWithExpireAfter(Constant.KEY_REDIS.POST + postId, JSONUtils.objToJsonString(postUpdate.get()), Constant.TIME.SECOND_OF_ONE_DAY);
    }
}
