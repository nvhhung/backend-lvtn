package hcmut.cse.travelsocialnetwork.controller;

import hcmut.cse.travelsocialnetwork.application.like.ILikeApplication;
import hcmut.cse.travelsocialnetwork.command.comment.CommandComment;
import hcmut.cse.travelsocialnetwork.command.like.CommandLike;
import hcmut.cse.travelsocialnetwork.factory.AbstractController;
import hcmut.cse.travelsocialnetwork.utils.JSONUtils;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author : hung.nguyen23
 * @since : 9/20/22 Tuesday
 **/
@Component
public class LikeController extends AbstractController {
    private static final Logger log = LogManager.getLogger(LikeController.class);
    ILikeApplication likeApplication;

    public LikeController(ILikeApplication likeApplication) {
        this.likeApplication = likeApplication;
    }

    public void createLike(RoutingContext routingContext) {
        try {
            var userId = routingContext.user().principal().getString("userId");
            var commandLike = JSONUtils.jsonToObj(routingContext.getBodyAsString(), CommandLike.class);
            commandLike.setUserId(userId);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(this.outputJson(9999, likeApplication.createLike(commandLike)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(this.outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }

    public void unlike(RoutingContext routingContext) {
        try {
            var userId = routingContext.user().principal().getString("userId");
            var commandLike = JSONUtils.jsonToObj(routingContext.getBodyAsString(), CommandLike.class);
            commandLike.setUserId(userId);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(this.outputJson(9999, likeApplication.unlike(commandLike)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(this.outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }
}
