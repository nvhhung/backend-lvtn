package hcmut.cse.travelsocialnetwork.application.comment;

import hcmut.cse.travelsocialnetwork.application.user.HelperUser;
import hcmut.cse.travelsocialnetwork.command.comment.CommandComment;
import hcmut.cse.travelsocialnetwork.model.Comment;
import hcmut.cse.travelsocialnetwork.model.Post;
import hcmut.cse.travelsocialnetwork.repository.post.IPostRepository;
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
 * @since : 9/7/22 Wednesday
 **/
@Component
public class CommentApplication implements ICommentApplication{
    private static final Logger log = LogManager.getLogger(CommentApplication.class);
    @Autowired
    ICommentApplication commentApplication;
    @Autowired
    HelperUser helperUser;
    @Autowired
    ICommentApplication postRepository;
    @Autowired
    private UserRedis userRedis;

    @Override
    public Optional<Comment> createComment(CommandComment commandComment) throws Exception {
        var user = userRedis.getUser(commandComment.getUserId());
        var comment = Comment.builder()
                .postId(commandComment.getPostId())
                .userId(commandComment.getUserId())
                .content(commandComment.getContent())
                .build();
        var postAdd = postRepository.add(post);
        if (postAdd.isEmpty()) {
            log.info(String.format("create post by user = %s fail", user.getFullName()));
            throw new CustomException(Constant.ERROR_MSG.POST_FAIL);
        }
        return postAdd;
    }
}
