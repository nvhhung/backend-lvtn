package hcmut.cse.travelsocialnetwork;

import hcmut.cse.travelsocialnetwork.controller.*;
import hcmut.cse.travelsocialnetwork.service.VertxProvider;
import hcmut.cse.travelsocialnetwork.service.vertx.rest.RequestHandler;
import hcmut.cse.travelsocialnetwork.service.vertx.rest.RestfulVerticle;
import io.vertx.core.http.HttpMethod;
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
	LikeController likeController;
	RateController rateController;
	RankController rankController;
	GlobalConfigController globalConfigController;

	public TravelSocialNetworkApplication(RestfulVerticle restfulVerticle,
										  VertxProvider vertxProvider,
										  UserController userController,
										  PostController postController,
										  CommentController commentController,
										  FollowController followController,
										  LikeController likeController,
										  RateController rateController,
										  RankController rankController,
										  GlobalConfigController globalConfigController) {
		this.restfulVerticle = restfulVerticle;
		this.vertxProvider = vertxProvider;
		this.userController = userController;
		this.postController = postController;
		this.commentController = commentController;
		this.followController = followController;
		this.likeController = likeController;
		this.rateController = rateController;
		this.rankController = rankController;
		this.globalConfigController = globalConfigController;
	}

	public static void main(String[] args) {
		SpringApplication.run(TravelSocialNetworkApplication.class, args);
	}

	@PostConstruct
	public void deployServerVerticle() {
		final boolean auth = true;
		final boolean notAuth = false;
		restfulVerticle.setRequestHandlerList(Arrays.asList(
				// user
				RequestHandler.init(HttpMethod.GET, "/", userController::root, notAuth),
				RequestHandler.init(HttpMethod.POST, "/user/register", userController::register, notAuth),
				RequestHandler.init(HttpMethod.POST, "/user/login", userController::login, notAuth),
				RequestHandler.init(HttpMethod.POST, "/user/refresh-token", userController::refreshToken, notAuth),

				RequestHandler.init(HttpMethod.GET, "/user/info-user", userController::getInfoUser, notAuth),
				RequestHandler.init(HttpMethod.POST, "/user/update-info-user", userController::updateInfoUser, auth),
				RequestHandler.init(HttpMethod.POST, "/user/search", userController::search, notAuth),

				// follow
				RequestHandler.init(HttpMethod.POST, "/follow/follow-user", followController::followUser, auth),
				RequestHandler.init(HttpMethod.POST, "/follow/unfollow-user", followController::unFollowUser, auth),
				RequestHandler.init(HttpMethod.GET, "/follow/get-follower", followController::getFollower, auth),
				RequestHandler.init(HttpMethod.GET, "/follow/get-follow-user", followController::getFollowUser, auth),

				// comment
				RequestHandler.init(HttpMethod.POST, "/comment/create", commentController::createComment, auth),
				RequestHandler.init(HttpMethod.GET, "/comment/delete", commentController::deleteComment, auth),
				RequestHandler.init(HttpMethod.POST, "/comment/update", commentController::updateComment, auth),
				RequestHandler.init(HttpMethod.POST, "/comment/load", commentController::loadComment, notAuth),

				// like
				RequestHandler.init(HttpMethod.POST, "/like/create", likeController::createLike, auth),
				RequestHandler.init(HttpMethod.POST, "/like/unlike", likeController::unlike, auth),

				// rate
				RequestHandler.init(HttpMethod.POST, "/rate/create", rateController::rate, auth),
				RequestHandler.init(HttpMethod.POST, "/rate/delete", rateController::unRate, auth),

				// rank
				RequestHandler.init(HttpMethod.GET, "/rank/get-leader-board-user", rankController::getLeaderBoardUser, notAuth),
				RequestHandler.init(HttpMethod.GET, "/rank/info-rank-user", rankController::getRankUser, auth),
				RequestHandler.init(HttpMethod.GET, "/rank/get-leader-board-post", rankController::getLeaderBoardPost, notAuth),
				RequestHandler.init(HttpMethod.GET, "/rank/info-rank-board", rankController::getRankPost, auth),

				// post
				RequestHandler.init(HttpMethod.POST, "/post/create", postController::createPost, auth),
				RequestHandler.init(HttpMethod.POST, "/post/update", postController::updatePost, auth),
				RequestHandler.init(HttpMethod.POST, "/post/get", postController::getPost, notAuth),
				RequestHandler.init(HttpMethod.POST, "/post/load-all", postController::loadAllPost, notAuth),
				RequestHandler.init(HttpMethod.POST, "/post/load-related", postController::loadRelatedPost, auth),
				RequestHandler.init(HttpMethod.POST, "/post/search", postController::searchPost, notAuth),
				RequestHandler.init(HttpMethod.POST, "/post/delete", postController::deletePost, auth),

				// global config
				RequestHandler.init(HttpMethod.POST, "/global-config/create", globalConfigController::addGlobalConfig, notAuth),
				RequestHandler.init(HttpMethod.POST, "/global-config/delete", globalConfigController::deleteGlobalConfig, notAuth),
				RequestHandler.init(HttpMethod.POST, "/global-config/update", globalConfigController::updateGlobalConfig, notAuth),
				RequestHandler.init(HttpMethod.POST, "/global-config/load", globalConfigController::loadGlobalConfig, notAuth)
				));

		vertxProvider.getVertx().deployVerticle(restfulVerticle);
	}
}
