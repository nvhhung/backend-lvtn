package hcmut.cse.travelsocialnetwork.application.post;

import hcmut.cse.travelsocialnetwork.application.user.HelperUser;
import hcmut.cse.travelsocialnetwork.command.post.CommandPost;
import hcmut.cse.travelsocialnetwork.command.user.CommandLogin;
import hcmut.cse.travelsocialnetwork.model.LoginToken;
import hcmut.cse.travelsocialnetwork.model.Post;
import hcmut.cse.travelsocialnetwork.repository.user.IUserRepository;
import hcmut.cse.travelsocialnetwork.service.jwt.JWTAuth;
import hcmut.cse.travelsocialnetwork.service.redis.UserRedis;
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
    @Autowired
    HelperUser helperUser;
    @Autowired
    IUserRepository userRepository;
    @Autowired
    private JWTAuth jwtAuth;
    @Autowired
    private UserRedis redis;

    @Override
    public Optional<Post> createPost(CommandPost commandPost) throws Exception {
        return Optional.empty();
    }
}
