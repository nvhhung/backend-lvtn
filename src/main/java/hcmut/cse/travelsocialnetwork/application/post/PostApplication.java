package hcmut.cse.travelsocialnetwork.application.post;

import hcmut.cse.travelsocialnetwork.application.user.HelperUser;
import hcmut.cse.travelsocialnetwork.command.post.CommandPost;
import hcmut.cse.travelsocialnetwork.model.Post;
import hcmut.cse.travelsocialnetwork.repository.post.IPostRepository;
import hcmut.cse.travelsocialnetwork.service.elasticsearch.ElasticsearchClient;
import hcmut.cse.travelsocialnetwork.service.redis.UserRedis;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.CustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author : hung.nguyen23
 * @since : 8/30/22 Tuesday
 **/
@Component
public class PostApplication implements IPostApplication{
    private static final Logger log = LogManager.getLogger(PostApplication.class);

    HelperUser helperUser;
    IPostRepository postRepository;
    UserRedis userRedis;
    @Autowired
    public PostApplication(HelperUser helperUser,
                           IPostRepository postRepository,
                           UserRedis userRedis) {
        this.helperUser = helperUser;
        this.postRepository = postRepository;
        this.userRedis = userRedis;
    }

    @Override
    public Optional<Post> createPost(CommandPost commandPost) throws Exception {
        var user = userRedis.getUser(commandPost.getUserId());
        var post = Post.builder()
                .userId(commandPost.getUserId())
                .content(commandPost.getContent())
                .link(commandPost.getLink())
                .type(commandPost.getType())
                .commentSize(0)
                .likeSize(0)
                .status(commandPost.getStatus())
                .blackListUsers(commandPost.getBlackListUsers())
                .point(0L)
                .build();
        var postAdd = postRepository.add(post);
        if (postAdd.isEmpty()) {
            log.info(String.format("create post by user = %s fail", user.getFullName()));
            throw new CustomException(Constant.ERROR_MSG.POST_FAIL);
        }
        return postAdd;
    }

    @Override
    public Optional<Post> updatePost(CommandPost commandPost) throws Exception {
        var postTemp = postRepository.getById(commandPost.getId());
        if (postTemp.isEmpty()) {
            log.error(String.format("not found post have id = %s", commandPost.getId()));
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUND_POST);
        }
        var userPost = userRedis.getUser(commandPost.getUserId());
        var post = postTemp.get();
        Optional.ofNullable(commandPost.getTitle()).ifPresent(post::setTitle);
        Optional.ofNullable(commandPost.getContent()).ifPresent(post::setContent);
        Optional.ofNullable(commandPost.getLink()).ifPresent(post::setLink);
        Optional.ofNullable(commandPost.getStatus()).ifPresent(post::setStatus);
        Optional.ofNullable(commandPost.getType()).ifPresent(post::setType);
        post.setLastUpdateTime(System.currentTimeMillis());

        var postUpdate = postRepository.update(post.get_id().toString(), post);
        if (postUpdate.isEmpty()) {
            log.info(String.format("update post have id = %s fail",post.get_id()));
            throw new CustomException(Constant.ERROR_MSG.UPDATE_POST_FAIL);
        }
        log.info(String.format("update post have id = %s by user %s successful",post.get_id(),userPost.getFullName()));
        return postUpdate;
    }

    @Override
    public Optional<Post> getPost(CommandPost commandPost) throws Exception {
        var postTemp = postRepository.getById(commandPost.getId());
        if (postTemp.isEmpty()) {
            log.error(String.format("not found post have id = %s", commandPost.getId()));
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUND_POST);
        }
        return postTemp;
    }

    @Override
    public Optional<Boolean> deletePost(CommandPost commandPost) throws Exception {
        var postTemp = postRepository.getById(commandPost.getId());
        if (postTemp.isEmpty()) {
            log.error(String.format("not found post have id = %s", commandPost.getId()));
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUND_POST);
        }
        return postRepository.delete(commandPost.getId());
    }
}
