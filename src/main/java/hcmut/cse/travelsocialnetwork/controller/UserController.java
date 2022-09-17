package hcmut.cse.travelsocialnetwork.controller;

import hcmut.cse.travelsocialnetwork.application.user.IUserApplication;
import hcmut.cse.travelsocialnetwork.command.user.CommandLogin;
import hcmut.cse.travelsocialnetwork.command.user.CommandRegister;
import hcmut.cse.travelsocialnetwork.command.user.CommandUser;
import hcmut.cse.travelsocialnetwork.factory.AbstractController;
import hcmut.cse.travelsocialnetwork.utils.JSONUtils;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class UserController extends AbstractController {
    private static final Logger log = LogManager.getLogger(UserController.class);

    @Autowired
    IUserApplication userApplication;

    public void root(RoutingContext routingContext) {
        try {
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(outputJson(9999, new HashMap<>()));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }

    public void register(RoutingContext routingContext) {
        try {
            var commandRegister = JSONUtils.stringToObj(routingContext.getBodyAsString(), CommandRegister.class);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(outputJson(9999, userApplication.register(commandRegister)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }
//
//    public void refreshToken(RoutingContext routingContext) {
//        try {
//            MultiMap params = routingContext.request().params();
//            String access_token = params.get("access_token");
//            String refresh_token = params.get("refresh_token");
//            routingContext.response()
//                    .setStatusCode(200)
//                    .putHeader("Content-Type", "application/json; charset=utf-8")
//                    .end(this.outputJson(9999, userApplication.refreshToken(access_token, refresh_token)));
//        } catch (Throwable throwable) {
//            LOGGER.error(throwable);
//            routingContext.response()
//                    .setStatusCode(200)
//                    .putHeader("content-type", "application/json; charset=utf-8")
//                    .end(this.outputJson(-9999, throwable.getMessage(), new HashMap<>()));
//        }
//
//    }

    public void login(RoutingContext routingContext) {
        try {
            var commandLogin = JSONUtils.stringToObj(routingContext.getBodyAsString(), CommandLogin.class);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(outputJson(9999, userApplication.login(commandLogin).orElse(null)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }

    public void getInfoUser(RoutingContext routingContext) {
        try {
            MultiMap params = routingContext.request().params();
            String userId = params.get("userId");
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(this.outputJson(9999, userApplication.getInfoUser(userId)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(this.outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }

    }

    public void updateInfoUser(RoutingContext routingContext) {
        try {
            var commandUser = JSONUtils.stringToObj(routingContext.getBodyAsString(), CommandUser.class);
            commandUser.setUserId(routingContext.user().get("userId"));
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(this.outputJson(9999, userApplication.updateInfoUser(commandUser)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(this.outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }
}
