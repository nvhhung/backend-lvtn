package hcmut.cse.travelsocialnetwork.controller;

import hcmut.cse.travelsocialnetwork.application.follow.IFollowApplication;
import hcmut.cse.travelsocialnetwork.command.follow.CommandFollow;
import hcmut.cse.travelsocialnetwork.factory.AbstractController;
import hcmut.cse.travelsocialnetwork.utils.JSONUtils;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author : hung.nguyen23
 * @since : 9/17/22 Saturday
 **/
@Component
public class FollowController extends AbstractController {
    private static final Logger log = LogManager.getLogger(FollowController.class);
    IFollowApplication followApplication;

    public FollowController(IFollowApplication followApplication) {
        this.followApplication = followApplication;
    }

    public void followUser(RoutingContext routingContext) {
        try {
            var commandFollow = JSONUtils.stringToObj(routingContext.getBodyAsString(), CommandFollow.class);
            commandFollow.setUserId(routingContext.user().get("userId"));
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(outputJson(9999, followApplication.followUser(commandFollow)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }

    public void unFollowUser(RoutingContext routingContext) {
        try {
            var commandFollow = JSONUtils.stringToObj(routingContext.getBodyAsString(), CommandFollow.class);
            commandFollow.setUserId(routingContext.user().get("userId"));
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(outputJson(9999, followApplication.unFollowUser(commandFollow)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }

    public void getFollower(RoutingContext routingContext) {
        try {
            var commandFollow = new CommandFollow();
            commandFollow.setUserId(routingContext.user().get("userId"));
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(outputJson(9999, followApplication.getFollower(commandFollow)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }

    public void getFollowUser(RoutingContext routingContext) {
        try {
            var commandFollow = new CommandFollow();
            commandFollow.setUserId(routingContext.user().get("userId"));
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(outputJson(9999, followApplication.getFollowUser(commandFollow)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }
}
