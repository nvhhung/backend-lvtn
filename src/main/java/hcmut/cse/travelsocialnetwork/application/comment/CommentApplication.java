package hcmut.cse.travelsocialnetwork.application.comment;

import hcmut.cse.travelsocialnetwork.application.user.HelperUser;
import hcmut.cse.travelsocialnetwork.command.comment.CommandComment;
import hcmut.cse.travelsocialnetwork.model.Comment;
import hcmut.cse.travelsocialnetwork.repository.comment.ICommentRepository;
import hcmut.cse.travelsocialnetwork.service.redis.PostRedis;
import hcmut.cse.travelsocialnetwork.service.redis.UserRedis;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.CustomException;
import hcmut.cse.travelsocialnetwork.utils.enums.FactorialPost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author : hung.nguyen23
 * @since : 9/7/22 Wednesday
 **/
@Component
public class CommentApplication implements ICommentApplication{
    private static final Logger log = LogManager.getLogger(CommentApplication.class);
    HelperUser helperUser;
    UserRedis userRedis;
    ICommentRepository commentRepository;
    PostRedis postRedis;

    public CommentApplication(HelperUser helperUser,
                              UserRedis userRedis,
                              ICommentRepository commentRepository,
                              PostRedis postRedis) {
        this.helperUser = helperUser;
        this.userRedis = userRedis;
        this.commentRepository = commentRepository;
        this.postRedis = postRedis;
    }

    @Override
    public Optional<Comment> createComment(CommandComment commandComment) throws Exception {
        var comment = Comment.builder()
                .postId(commandComment.getPostId())
                .userId(commandComment.getUserId())
                .content(commandComment.getContent())
                .images(commandComment.getImages())
                .build();
        var commentAdd = commentRepository.add(comment);
        if (commentAdd.isEmpty()) {
            log.warn(String.format("%s create comment fail", commandComment.getUserId()));
            throw new CustomException(Constant.ERROR_MSG.COMMENT_FAIL);
        }
        postRedis.increaseFactorial(commandComment.getPostId(), FactorialPost.COMMENT);
        postRedis.increasePoints(commandComment.getPostId(), Constant.POINTS.ONE_COMMENT_POST);
        userRedis.increasePoints(commandComment.getUserId(), Constant.POINTS.ONE_COMMENT_USER);
        // todo : push notification owner post
        return commentAdd;
    }

    @Override
    public Optional<Boolean> deleteComment(CommandComment commandComment) throws Exception {
        var query = new Document("userId", commandComment.getUserId()).append("postId", commandComment.getPostId());
        var comment = commentRepository.get(query);
        if (comment.isEmpty()) {
            log.warn(String.format("%s not found comment in post %s", commandComment.getUserId(), commandComment.getPostId()));
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUND_COMMENT);
        }

        postRedis.decreaseFactorial(commandComment.getPostId(), FactorialPost.COMMENT);
        postRedis.decreasePoints(commandComment.getPostId(), Constant.POINTS.ONE_COMMENT_POST);
        userRedis.decreasePoints(commandComment.getUserId(), Constant.POINTS.ONE_COMMENT_USER);
        return commentRepository.delete(comment.get().get_id().toString());
    }


    @Override
    public Optional<List<Comment>> loadComment(CommandComment commandComment) throws Exception {
        var post = postRedis.getPost(commandComment.getPostId());
        if (post == null) {
            log.warn(String.format("%s not found post", commandComment.getUserId()));
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUND_POST);
        }
        var query = new Document("postId",commandComment.getPostId());
        var commentList = commentRepository.search(query,new Document("createTime", -1), commandComment.getPage(), commandComment.getSize());
        if (commentList.isEmpty()) {
            log.warn(String.format("%s no have comment", commandComment.getPostId()));
            return Optional.ofNullable(new ArrayList<>());
        }
        return commentList;
    }
}
