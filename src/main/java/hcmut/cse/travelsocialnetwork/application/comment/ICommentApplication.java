package hcmut.cse.travelsocialnetwork.application.comment;

import hcmut.cse.travelsocialnetwork.command.comment.CommandComment;
import hcmut.cse.travelsocialnetwork.model.Comment;

import java.util.Optional;

/**
 * @author : hung.nguyen23
 * @since : 9/7/22 Wednesday
 **/
public interface ICommentApplication {
    Optional<Comment> createComment(CommandComment commandComment) throws Exception;
}
