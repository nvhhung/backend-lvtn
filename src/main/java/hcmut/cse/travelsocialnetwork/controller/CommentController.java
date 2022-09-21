package hcmut.cse.travelsocialnetwork.controller;

import hcmut.cse.travelsocialnetwork.application.comment.ICommentApplication;
import hcmut.cse.travelsocialnetwork.command.comment.CommandComment;
import hcmut.cse.travelsocialnetwork.factory.AbstractController;
import hcmut.cse.travelsocialnetwork.utils.JSONUtils;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author : hung.nguyen23
 * @since : 9/7/22 Wednesday
 **/
@Component
public class CommentController extends AbstractController {
    private static final Logger log = LogManager.getLogger(CommentController.class);
    ICommentApplication commentApplication;

    public CommentController(ICommentApplication commentApplication) {
        this.commentApplication = commentApplication;
    }

    public void createComment(RoutingContext routingContext) {
        try {
            var userId = routingContext.user().principal().getString("userId");
            var commandComment = JSONUtils.jsonToObj(routingContext.getBodyAsString(), CommandComment.class);
            commandComment.setUserId(userId);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(this.outputJson(9999, commentApplication.createComment(commandComment)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(this.outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }

    public void deleteComment(RoutingContext routingContext) {
        try {
            var userId = routingContext.user().principal().getString("userId");
            var commandComment = JSONUtils.jsonToObj(routingContext.getBodyAsString(), CommandComment.class);
            commandComment.setUserId(userId);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(this.outputJson(9999, commentApplication.deleteComment(commandComment)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(this.outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }

    public void loadComment(RoutingContext routingContext) {
        try {
            MultiMap params = routingContext.request().params();
            var commandComment = CommandComment.builder()
                    .postId(params.get("postId"))
                    .page(Integer.parseInt(params.get("page")))
                    .size(Integer.parseInt(params.get("size")))
                    .build();
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(this.outputJson(9999, commentApplication.loadComment(commandComment)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(this.outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }
}
