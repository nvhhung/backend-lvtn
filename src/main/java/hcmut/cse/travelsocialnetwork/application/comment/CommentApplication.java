package hcmut.cse.travelsocialnetwork.application.comment;

import hcmut.cse.travelsocialnetwork.application.user.HelperUser;
import hcmut.cse.travelsocialnetwork.command.comment.CommandComment;
import hcmut.cse.travelsocialnetwork.model.Comment;
import hcmut.cse.travelsocialnetwork.repository.comment.ICommentRepository;
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
    RankRedis rankRedis;

    public CommentApplication(HelperUser helperUser,
                              UserRedis userRedis,
                              ICommentRepository commentRepository,
                              PostRedis postRedis,
                              RankRedis rankRedis) {
        this.helperUser = helperUser;
        this.userRedis = userRedis;
        this.commentRepository = commentRepository;
        this.postRedis = postRedis;
        this.rankRedis = rankRedis;
    }

    @Override
    public Optional<Comment> createComment(CommandComment commandComment) throws Exception {
        var comment = Comment.builder()
                .postId(commandComment.getPostId())
                .userId(commandComment.getUserId())
                .content(commandComment.getContent())
                .build();
        var commentAdd = commentRepository.add(comment);
        if (commentAdd.isEmpty()) {
            log.warn(String.format("%s create comment fail", commandComment.getUserId()));
            throw new CustomException(Constant.ERROR_MSG.COMMENT_FAIL);
        }
        postRedis.increaseFactorial(commandComment.getPostId(), FactorialPost.COMMENT);
        var pointPostNew = postRedis.increaseAndGetPoints(commandComment.getPostId(), Constant.POINTS.ONE_COMMENT_POST);
        rankRedis.addLeaderBoard(Constant.LEADER_BOARD.KEY_POST, commandComment.getPostId(), pointPostNew);

        var pointUserNew = userRedis.increaseAndGetPoints(commandComment.getUserId(), Constant.POINTS.ONE_COMMENT_USER);
        rankRedis.addLeaderBoard(Constant.LEADER_BOARD.KEY_USER, commandComment.getUserId(), pointUserNew);
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
        var pointPostNew = postRedis.decreaseAndGetPoints(commandComment.getPostId(), Constant.POINTS.ONE_COMMENT_POST);
        rankRedis.addLeaderBoard(Constant.LEADER_BOARD.KEY_POST, commandComment.getPostId(), pointPostNew);

        var pointUserNew = userRedis.decreaseAndGetPoints(commandComment.getUserId(), Constant.POINTS.ONE_COMMENT_USER);
        rankRedis.addLeaderBoard(Constant.LEADER_BOARD.KEY_USER, commandComment.getUserId(), pointUserNew);

        return commentRepository.delete(comment.get().get_id().toString());
    }


    @Override
    public Optional<List<Comment>> loadComment(CommandComment commandComment) throws Exception {
        var post = postRedis.getPost(commandComment.getPostId());
        if (post == null) {
            log.warn(String.format("%s not found post", commandComment.getUserId()));
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUND_POST);
        }
        var query = new Document(Constant.FIELD_QUERY.POST_ID,commandComment.getPostId());
        var commentList = commentRepository.search(query,new Document(Constant.FIELD_QUERY.CREATE_TIME, -1), commandComment.getPage(), commandComment.getSize());
        if (commentList.isEmpty()) {
            log.warn(String.format("%s no have comment", commandComment.getPostId()));
            return Optional.ofNullable(new ArrayList<>());
        }
        return commentList;
    }
}
