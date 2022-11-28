package hcmut.cse.travelsocialnetwork.controller;

import hcmut.cse.travelsocialnetwork.application.notification.INotificationApplication;
import hcmut.cse.travelsocialnetwork.command.notification.CommandNotification;
import hcmut.cse.travelsocialnetwork.factory.AbstractController;
import hcmut.cse.travelsocialnetwork.factory.configuration.ENVConfig;
import hcmut.cse.travelsocialnetwork.utils.JSONUtils;
import io.ably.lib.rest.AblyRest;
import io.ably.lib.types.AblyException;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author : hung.nguyen23
 * @since : 8/31/22 Wednesday
 **/
@Component
public class NotificationController extends AbstractController {
    private static final Logger log = LogManager.getLogger(NotificationController.class);

    INotificationApplication notificationApplication;
    private final String CHANNEL_NAME = "default";
    private AblyRest ablyRest;
    private ENVConfig applicationConfig;

    public NotificationController(INotificationApplication notificationApplication,
                                  ENVConfig applicationConfig) throws AblyException {
        this.notificationApplication = notificationApplication;
        this.applicationConfig = applicationConfig;

        ablyRest = new AblyRest(applicationConfig.getStringProperty("application.ably_api_key", ""));
    }

    public void readNotify(RoutingContext routingContext) {
        try {
            var userId = routingContext.user().principal().getString("userId");
            var commandNotification = JSONUtils.jsonToObj(routingContext.getBodyAsString(), CommandNotification.class);
            commandNotification.setUserId(userId);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(this.outputJson(9999, notificationApplication.readNotification(commandNotification)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(this.outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }

    public void loadNotify(RoutingContext routingContext) {
        try {
            var userId = routingContext.user().principal().getString("userId");
            var commandNotification = JSONUtils.jsonToObj(routingContext.getBodyAsString(), CommandNotification.class);
            commandNotification.setUserId(userId);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json; charset=utf-8")
                    .end(this.outputJson(9999, notificationApplication.loadNotification(commandNotification)));
        } catch (Throwable throwable) {
            log.error(throwable);
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(this.outputJson(-9999, throwable.getMessage(), new HashMap<>()));
        }
    }
}
