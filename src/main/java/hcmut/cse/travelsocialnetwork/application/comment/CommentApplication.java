package hcmut.cse.travelsocialnetwork.application.comment;

import hcmut.cse.travelsocialnetwork.application.notification.INotificationApplication;
import hcmut.cse.travelsocialnetwork.command.comment.CommandComment;
import hcmut.cse.travelsocialnetwork.command.notification.CommandNotification;
import hcmut.cse.travelsocialnetwork.model.Comment;
import hcmut.cse.travelsocialnetwork.model.Paginated;
import hcmut.cse.travelsocialnetwork.repository.comment.ICommentRepository;
import hcmut.cse.travelsocialnetwork.service.redis.PostRedis;
import hcmut.cse.travelsocialnetwork.service.redis.RankRedis;
import hcmut.cse.travelsocialnetwork.service.redis.UserRedis;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.CustomException;
import hcmut.cse.travelsocialnetwork.utils.JSONUtils;
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

    UserRedis userRedis;
    ICommentRepository commentRepository;
    PostRedis postRedis;
    RankRedis rankRedis;
    INotificationApplication notificationApplication;

    public CommentApplication(UserRedis userRedis,
                              ICommentRepository commentRepository,
                              PostRedis postRedis,
                              RankRedis rankRedis,
                              INotificationApplication notificationApplication) {
        this.userRedis = userRedis;
        this.commentRepository = commentRepository;
        this.postRedis = postRedis;
        this.rankRedis = rankRedis;
        this.notificationApplication = notificationApplication;
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
        //push notification owner post
        var post = postRedis.getPost(comment.getPostId());
        var userTrigger = userRedis.getUser(comment.getUserId());
        var userIdOwner = post.getUserId();
        var commandNotification = CommandNotification.builder()
                .userId(userIdOwner)
                .userIdTrigger(userTrigger.get_id().toString())
                .isRead(false)
                .objectId(post.get_id().toString())
                .type(Constant.NOTIFICATION.COMMENT)
                .channel(Constant.NOTIFICATION.COMMENT)
                .title(String.format(Constant.NOTIFICATION.TITLE_POST, post.getTitle()))
                .content(String.format(Constant.NOTIFICATION.CONTENT_COMMENT, userTrigger.getFullName(), post.getTitle()))
                .build();
        notificationApplication.createNotification(commandNotification);
        // push notification to user comment post
        var query = new Document(Constant.FIELD_QUERY.POST_ID,commandComment.getPostId());
        query.append(Constant.FIELD_QUERY.USER_ID, new Document("$ne", userIdOwner));
        var sort = new Document(Constant.FIELD_QUERY.CREATE_TIME, -1);
        var commentList = commentRepository.search(query, sort, 1, 1000);
        if (commentList.isEmpty() || commentList.get().isEmpty()) {
            log.info("no have other user comment in post");
            return commentAdd;
        }

        commentList.get().forEach(commentUserOther -> {
            var commandNotificationOther = CommandNotification.builder()
                    .userId(commentUserOther.getUserId())
                    .userIdTrigger(userTrigger.get_id().toString())
                    .isRead(false)
                    .objectId(post.get_id().toString())
                    .type(Constant.NOTIFICATION.COMMENT)
                    .channel(Constant.NOTIFICATION.COMMENT)
                    .title(String.format(Constant.NOTIFICATION.TITLE_POST, post.getTitle()))
                    .content(String.format(Constant.NOTIFICATION.CONTENT_COMMENT, userTrigger.getFullName(), post.getTitle()))
                    .build();
            try {
                // TODO: publish message to channel of owner post
                notificationApplication.createNotification(commandNotificationOther);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return commentAdd;
    }

    @Override
    public Optional<Boolean> deleteComment(CommandComment commandComment) throws Exception {
        var commentOptional = commentRepository.getById(commandComment.getCommentId());
        if (commentOptional.isEmpty()) {
            log.error(String.format("not found comment have id = %s", commandComment.getCommentId()));
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUND_COMMENT);
        }
        var comment = commentOptional.get();

        postRedis.decreaseFactorial(commandComment.getPostId(), FactorialPost.COMMENT);
        var pointPostNew = postRedis.decreaseAndGetPoints(commandComment.getPostId(), Constant.POINTS.ONE_COMMENT_POST);
        rankRedis.addLeaderBoard(Constant.LEADER_BOARD.KEY_POST, commandComment.getPostId(), pointPostNew);

        var pointUserNew = userRedis.decreaseAndGetPoints(commandComment.getUserId(), Constant.POINTS.ONE_COMMENT_USER);
        rankRedis.addLeaderBoard(Constant.LEADER_BOARD.KEY_USER, commandComment.getUserId(), pointUserNew);

        return commentRepository.delete(comment.get_id().toString());
    }


    @Override
    public Optional<Object> loadComment(CommandComment commandComment) throws Exception {
        var post = postRedis.getPost(commandComment.getPostId());
        if (post == null) {
            log.warn(String.format("%s not found post", commandComment.getUserId()));
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUND_POST);
        }
        var query = new Document(Constant.FIELD_QUERY.POST_ID,commandComment.getPostId());
        var sort = new Document(Constant.FIELD_QUERY.CREATE_TIME, -1);
        var totalItem = commentRepository.count(query);
        var commentList = commentRepository.search(query, sort, commandComment.getPage(), commandComment.getSize());
        var listResult = convertMappingComment(commentList.orElse(new ArrayList<>()));
        log.info(String.format("post %s have rate size %d", commandComment.getPostId(), listResult.size()));
        return Optional.of(new Paginated<>(listResult, commandComment.getPage(), commandComment.getSize(), totalItem.orElse(0L)));
    }

    private List<Object>  convertMappingComment(List<Comment> commentList) {
        var result = new ArrayList<>();
        commentList.forEach(comment -> {
            var mapComment = JSONUtils.objToMap(comment);
            if (mapComment != null) {
                var user = userRedis.getUser(comment.getUserId());
                mapComment.put("avatar", user.getAvatar());
                mapComment.put("fullName", user.getFullName());
            }
            result.add(mapComment);
        });
        return result;
    }

    @Override
    public Optional<Comment> updateComment(CommandComment commandComment) throws Exception {
        var commentOptional = commentRepository.getById(commandComment.getCommentId());
        if (commentOptional.isEmpty()) {
            log.error(String.format("not found comment have id = %s", commandComment.getCommentId()));
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUND_COMMENT);
        }
        var comment = commentOptional.get();
        Optional.ofNullable(commandComment.getContent()).ifPresent(comment::setContent);

        var commentUpdate = commentRepository.update(comment.get_id().toString(), comment);
        if (commentUpdate.isEmpty()) {
            log.info(String.format("update comment have id = %s fail", comment.get_id()));
            throw new CustomException(Constant.ERROR_MSG.UPDATE_COMMENT_FAIL);
        }
        return commentUpdate;
    }
}
