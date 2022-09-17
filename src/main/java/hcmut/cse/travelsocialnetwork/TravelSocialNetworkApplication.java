package hcmut.cse.travelsocialnetwork;

import hcmut.cse.travelsocialnetwork.controller.CommentController;
import hcmut.cse.travelsocialnetwork.controller.FollowController;
import hcmut.cse.travelsocialnetwork.controller.PostController;
import hcmut.cse.travelsocialnetwork.controller.UserController;
import hcmut.cse.travelsocialnetwork.service.VertxProvider;
import hcmut.cse.travelsocialnetwork.service.vertx.rest.RequestHandler;
import hcmut.cse.travelsocialnetwork.service.vertx.rest.RestfulVerticle;
import io.vertx.core.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@ComponentScan(basePackages = {"hcmut.cse.travelsocialnetwork.*"})
public class TravelSocialNetworkApplication {

	RestfulVerticle restfulVerticle;
	VertxProvider vertxProvider;
	UserController userController;
	PostController postController;
	CommentController commentController;
	FollowController followController;

	public TravelSocialNetworkApplication(RestfulVerticle restfulVerticle,
										  VertxProvider vertxProvider,
										  UserController userController,
										  PostController postController,
										  CommentController commentController,
										  FollowController followController) {
		this.restfulVerticle = restfulVerticle;
		this.vertxProvider = vertxProvider;
		this.userController = userController;
		this.postController = postController;
		this.commentController = commentController;
		this.followController = followController;
	}

	public static void main(String[] args) {
		SpringApplication.run(TravelSocialNetworkApplication.class, args);
	}

	@PostConstruct
	public void deployServerVerticle() {
		final boolean auth = true, notAuth = false;
		restfulVerticle.setRequestHandlerList(Arrays.asList(
				// user
				RequestHandler.init(HttpMethod.GET, "/", userController::root, notAuth),
				RequestHandler.init(HttpMethod.POST, "/user/register", userController::register, notAuth),
				RequestHandler.init(HttpMethod.POST, "/user/login", userController::login, notAuth),
				RequestHandler.init(HttpMethod.GET, "/user/info-user", userController::getInfoUser, auth),
				RequestHandler.init(HttpMethod.POST, "/user/update-info-user", userController::updateInfoUser, auth),
				RequestHandler.init(HttpMethod.POST, "/user/search-user", userController::getInfoUser, auth),

				// follow
				RequestHandler.init(HttpMethod.POST, "/follow/follow-user", followController::followUser, auth),
				RequestHandler.init(HttpMethod.POST, "/follow/unfollow-user", followController::unFollowUser, auth),
				RequestHandler.init(HttpMethod.GET, "/follow/get-follower", followController::getFollower, auth),
				RequestHandler.init(HttpMethod.GET, "/follow/get-follow-user", followController::getFollowUser, auth),

				// user follow
				RequestHandler.init(HttpMethod.GET, "/user/get-follows", userController::root, notAuth),
				RequestHandler.init(HttpMethod.POST, "/user/follow-user", userController::root, notAuth),

				// rank
				RequestHandler.init(HttpMethod.GET, "/user/get-rank", userController::root, notAuth),
				RequestHandler.init(HttpMethod.GET, "/user/info-rank", userController::root, notAuth),

				// post
				RequestHandler.init(HttpMethod.POST, "/post/create", postController::createPost, auth),
				RequestHandler.init(HttpMethod.POST, "/post/update", postController::updatePost, auth),
				RequestHandler.init(HttpMethod.POST, "/post/get", postController::getPost, notAuth),
				RequestHandler.init(HttpMethod.POST, "/post/delete", postController::updatePost, auth)
				));

		vertxProvider.getVertx().deployVerticle(restfulVerticle);
	}
}
