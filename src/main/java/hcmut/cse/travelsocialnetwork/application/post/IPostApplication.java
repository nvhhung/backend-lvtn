package hcmut.cse.travelsocialnetwork.application.post;

import hcmut.cse.travelsocialnetwork.command.post.CommandPost;
import hcmut.cse.travelsocialnetwork.model.Post;

import java.util.List;
import java.util.Optional;

/**
 * @author : hung.nguyen23
 * @since : 8/30/22 Tuesday
 **/
public interface IPostApplication {
    Optional<Post> createPost(CommandPost commandPost) throws Exception;
    Optional<Post> updatePost(CommandPost commandPost) throws Exception;
    Optional<Post> getPost(CommandPost commandPost) throws Exception;
    Optional<Boolean> deletePost(CommandPost commandPost) throws Exception;
    Optional<Object> loadAllPost(CommandPost commandPost) throws Exception;
    Optional<Object> loadRelationPost(CommandPost commandPost) throws Exception;
    Optional<Object> searchPost(CommandPost commandPost) throws Exception;
    Optional<Object> loadByUserId(CommandPost commandPost) throws Exception;
}
