package hcmut.cse.travelsocialnetwork.controller;

import hcmut.cse.travelsocialnetwork.application.globalconfig.IGlobalConfigApplication;
import hcmut.cse.travelsocialnetwork.command.globalconfig.CommandGlobalConfig;
import hcmut.cse.travelsocialnetwork.factory.AbstractController;
import hcmut.cse.travelsocialnetwork.utils.JSONUtils;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author : hung.nguyen23
 * @since : 10/23/22 Sunday
 **/
@Component
public class GlobalConfigController extends AbstractController {

    private static final Logger log = LogManager.getLogger(GlobalConfigController.class);
    IGlobalConfigApplication globalConfigApplication;
    public GlobalConfigController(IGlobalConfigApplication globalConfigApplication) {
        this.globalConfigApplication = globalConfigApplication;
    }

    public void addGlobalConfig(RoutingContext routingContext) {
        try {
            var commandGlobalConfig = JSONUtils.stringToObj(routingContext.getBodyAsString(), CommandGlobalConfig.class);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(outputJson(9999, globalConfigApplication.createGlobalConfig(commandGlobalConfig)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }

    public void updateGlobalConfig(RoutingContext routingContext) {
        try {
            var commandGlobalConfig = JSONUtils.stringToObj(routingContext.getBodyAsString(), CommandGlobalConfig.class);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(outputJson(9999, globalConfigApplication.updateGlobalConfig(commandGlobalConfig)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }

    public void loadGlobalConfig(RoutingContext routingContext) {
        try {
            var commandGlobalConfig = JSONUtils.stringToObj(routingContext.getBodyAsString(), CommandGlobalConfig.class);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(outputJson(9999, globalConfigApplication.loadGlobalConfig(commandGlobalConfig)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }

    public void deleteGlobalConfig(RoutingContext routingContext) {
        try {
            var commandGlobalConfig = JSONUtils.stringToObj(routingContext.getBodyAsString(), CommandGlobalConfig.class);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(outputJson(9999, globalConfigApplication.deleteGlobalConfig(commandGlobalConfig)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }
}
