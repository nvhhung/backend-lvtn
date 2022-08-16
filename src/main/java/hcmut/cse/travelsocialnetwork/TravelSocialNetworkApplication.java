package hcmut.cse.travelsocialnetwork;

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

	public static void main(String[] args) {
		SpringApplication.run(TravelSocialNetworkApplication.class, args);
	}

	@PostConstruct
	public void deployServerVerticle() {
		final boolean auth = true, notAuth = false;
		restfulVerticle.setRequestHandlerList(Arrays.asList(
				RequestHandler.init(HttpMethod.GET, "/", userController::root, notAuth)
		));

		vertxProvider.getVertx().deployVerticle(restfulVerticle);
	}
}
