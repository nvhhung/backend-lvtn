package vn.hcmut.cse.travelsocialnetwork;

import controller.TravelSocialNetworkController;
import io.vertx.core.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import utils.vertx.RESTfulVerticle;
import utils.vertx.RequestHandler;
import utils.vertx.VertxProvider;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@ComponentScan({"utils","controller","configuration", "factory","ddd"})
public class TravelSocialNetworkApplication {
	@Autowired
	private RESTfulVerticle resTfulVerticle;
	@Autowired
	private VertxProvider vertxProvider;
	@Autowired
	private TravelSocialNetworkController travelSocialNetworkController;

	public static void main(String[] args) {
		SpringApplication.run(TravelSocialNetworkApplication.class, args);
	}

	@PostConstruct
	public void deployServerVerticle() {
		final boolean auth = true;
		final boolean not_auth = false;
		resTfulVerticle.setRequestHandlers(Arrays.asList(
				RequestHandler.init(HttpMethod.GET, "/", travelSocialNetworkController::root, not_auth)
		));
		vertxProvider.getVertx().deployVerticle(resTfulVerticle);
	}

}
