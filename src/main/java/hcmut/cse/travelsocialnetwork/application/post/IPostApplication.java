package hcmut.cse.travelsocialnetwork.application.post;

import hcmut.cse.travelsocialnetwork.command.post.CommandPost;
import hcmut.cse.travelsocialnetwork.command.user.CommandLogin;
import hcmut.cse.travelsocialnetwork.model.LoginToken;
import hcmut.cse.travelsocialnetwork.model.Post;

import java.util.Optional;

/**
 * @author : hung.nguyen23
 * @since : 8/30/22 Tuesday
 **/
public interface IPostApplication {
    Optional<Post> createPost(CommandPost commandPost) throws Exception;
}
