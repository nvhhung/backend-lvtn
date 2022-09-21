package hcmut.cse.travelsocialnetwork.controller;

import hcmut.cse.travelsocialnetwork.application.rate.IRateApplication;
import hcmut.cse.travelsocialnetwork.command.post.CommandPost;
import hcmut.cse.travelsocialnetwork.command.rate.CommandRate;
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
public class RateController extends AbstractController {
    private static final Logger log = LogManager.getLogger(RateController.class);
    IRateApplication rateApplication;

    public RateController(IRateApplication rateApplication) {
        this.rateApplication = rateApplication;
    }

    public void mark(RoutingContext routingContext) {
        try {
            var userId = routingContext.user().principal().getString("userId");
            var commandRate = JSONUtils.jsonToObj(routingContext.getBodyAsString(), CommandRate.class);
            commandRate.setUserId(userId);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(this.outputJson(9999, rateApplication.mark(commandRate)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(this.outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }
}
