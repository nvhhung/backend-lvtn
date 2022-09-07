package hcmut.cse.travelsocialnetwork;

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

	@Autowired
	private RestfulVerticle restfulVerticle;
	@Autowired
	private VertxProvider vertxProvider;
	@Autowired
	private UserController userController;
	@Autowired
	private PostController postController;

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
				RequestHandler.init(HttpMethod.POST, "/user/search-user", userController::getInfoUser, auth),

				// friend
				RequestHandler.init(HttpMethod.POST, "/user/create-friend-request", userController::root, notAuth),
				RequestHandler.init(HttpMethod.POST, "/user/delete-friend-request", userController::root, notAuth),
				RequestHandler.init(HttpMethod.GET, "/user/get-friend-request", userController::root, notAuth),
				RequestHandler.init(HttpMethod.GET, "/user/get-friends", userController::root, notAuth),
				RequestHandler.init(HttpMethod.POST, "/user/get-friends", userController::root, notAuth),

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
