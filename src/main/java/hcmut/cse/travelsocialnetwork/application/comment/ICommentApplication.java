package hcmut.cse.travelsocialnetwork.application.comment;

import hcmut.cse.travelsocialnetwork.command.comment.CommandComment;
import hcmut.cse.travelsocialnetwork.model.Comment;

import java.util.List;
import java.util.Optional;

/**
 * @author : hung.nguyen23
 * @since : 9/7/22 Wednesday
 **/
public interface ICommentApplication {
    Optional<Comment> createComment(CommandComment commandComment) throws Exception;
    Optional<Boolean> deleteComment(CommandComment commandComment) throws Exception;
    Optional<List<Object>> loadComment(CommandComment commandComment) throws Exception;
    Optional<Comment> updateComment(CommandComment commandComment) throws Exception;
}
