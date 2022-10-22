package hcmut.cse.travelsocialnetwork.controller;

import hcmut.cse.travelsocialnetwork.application.post.IPostApplication;
import hcmut.cse.travelsocialnetwork.command.post.CommandPost;
import hcmut.cse.travelsocialnetwork.factory.AbstractController;
import hcmut.cse.travelsocialnetwork.utils.JSONUtils;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author : hung.nguyen23
 * @since : 8/30/22 Tuesday
 **/
@Component
public class PostController extends AbstractController {
    private static final Logger log = LogManager.getLogger(PostController.class);
    IPostApplication postApplication;

    public PostController(IPostApplication postApplication) {
        this.postApplication = postApplication;
    }

    public void createPost(RoutingContext routingContext) {
        try {
            var userId = routingContext.user().principal().getString("userId");
            var commandPost = JSONUtils.jsonToObj(routingContext.getBodyAsString(), CommandPost.class);
            commandPost.setUserId(userId);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(this.outputJson(9999, postApplication.createPost(commandPost)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(this.outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }

    public void updatePost(RoutingContext routingContext) {
        try {
            var userId = routingContext.user().principal().getString("userId");
            var commandPost = JSONUtils.jsonToObj(routingContext.getBodyAsString(), CommandPost.class);
            commandPost.setUserId(userId);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(this.outputJson(9999, postApplication.updatePost(commandPost)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(this.outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }

    public void getPost(RoutingContext routingContext) {
        try {
            var commandPost = JSONUtils.jsonToObj(routingContext.getBodyAsString(), CommandPost.class);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(this.outputJson(9999, postApplication.getPost(commandPost)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(this.outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }

    public void deletePost(RoutingContext routingContext) {
        try {
            var commandPost = JSONUtils.jsonToObj(routingContext.getBodyAsString(), CommandPost.class);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(this.outputJson(9999, postApplication.deletePost(commandPost)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(this.outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }

    public void loadAllPost(RoutingContext routingContext) {
        try {
            var commandPost = JSONUtils.jsonToObj(routingContext.getBodyAsString(), CommandPost.class);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(this.outputJson(9999, postApplication.loadAllPost(commandPost)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(this.outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }

    public void loadRelatedPost(RoutingContext routingContext) {
        try {
            var commandPost = JSONUtils.jsonToObj(routingContext.getBodyAsString(), CommandPost.class);
            commandPost.setUserId(routingContext.user().principal().getString("userId"));
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(this.outputJson(9999, postApplication.loadRelationPost(commandPost)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(this.outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }

    public void searchPost(RoutingContext routingContext) {
        try {
            var commandPost = JSONUtils.jsonToObj(routingContext.getBodyAsString(), CommandPost.class);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(this.outputJson(9999, postApplication.searchPost(commandPost)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(this.outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }
}
