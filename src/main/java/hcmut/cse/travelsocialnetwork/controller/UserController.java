package hcmut.cse.travelsocialnetwork.controller;

import hcmut.cse.travelsocialnetwork.factory.AbstractController;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class UserController extends AbstractController {
    private static final Logger LOGGER = LogManager.getLogger();

    public void root(RoutingContext routingContext) {
        try {
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(outputJson(9999, new HashMap<>()));
        } catch (Throwable throwable) {
            LOGGER.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8");
//                    .end(outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }
}
