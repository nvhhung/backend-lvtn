package hcmut.cse.travelsocialnetwork.controller;

import hcmut.cse.travelsocialnetwork.application.comment.ICommentApplication;
import hcmut.cse.travelsocialnetwork.factory.AbstractController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * @author : hung.nguyen23
 * @since : 9/7/22 Wednesday
 **/
@Component
public class CommentController extends AbstractController {
    private static final Logger log = LogManager.getLogger(CommentController.class);
    @Autowired
    ICommentApplication commentApplication;

//    public void createComment(RoutingContext routingContext) {
//        try {
//            var userId = routingContext.user().principal().getString("userId");
//            var commandPost = JSONUtils.jsonToObj(routingContext.getBodyAsString(), CommandPost.class);
//            commandPost.setUserId(userId);
//            routingContext.response()
//                    .setStatusCode(200)
//                    .putHeader("Content-Type", "application/json; charset=utf-8")
//                    .end(this.outputJson(9999, commandPost.createPost(commandPost)));
//        } catch (Throwable throwable) {
//            log.error(throwable);
//            routingContext.response()
//                    .setStatusCode(200)
//                    .putHeader("content-type", "application/json; charset=utf-8")
//                    .end(this.outputJson(-9999, throwable.getMessage(), new HashMap<>()));
//        }
//    }
}
