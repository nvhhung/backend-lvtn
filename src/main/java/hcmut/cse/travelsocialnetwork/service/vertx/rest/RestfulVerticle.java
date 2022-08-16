package hcmut.cse.travelsocialnetwork.service.vertx.rest;

import hcmut.cse.travelsocialnetwork.factory.configuration.ENVConfig;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : hung.nguyen23
 * @since : 8/15/22 Monday
 **/
@Component
public class RestfulVerticle extends AbstractVerticle {
    private static Logger LOGGER = LogManager.getLogger();
    private List<RequestHandler> requestHandlerList = new ArrayList<>();
    @Autowired
    protected ENVConfig applicationConfig;
    @Autowired
    private JWTAuthHandler jwtAuthHandler;

    private void configureRoutes(Router router) {
    this.requestHandlerList.forEach(
        requestHandler -> {
          if (requestHandler.isEnableAuthentication()) {
            router
                .route(requestHandler.getPath())
                .handler(jwtAuthHandler)
                .handler(requestHandler.handle());
          } else {
            router
                .route(requestHandler.getMethod(), requestHandler.getPath())
                .handler(requestHandler.handle());
          }
          System.out.println("Configuring route {}:{}" + requestHandler.getMethod() + requestHandler.getPath());
        });
    }

    @Override
    public void start(Promise<Void> startPromise) {
        var router = Router.router(vertx);
        router.route().handler(BodyHandler.create()
                .setBodyLimit(5 * 1024 * 1024) // 5MB
                .setDeleteUploadedFilesOnEnd(true)
                .setUploadsDirectory(applicationConfig.getStringProperty("application.upload.director"))
        );
        router.route().handler(CorsHandler.create("*")
                .allowedMethod(HttpMethod.GET)
                .allowedMethod(HttpMethod.POST)
                .allowedMethod(HttpMethod.HEAD)
                .allowedMethod(HttpMethod.PUT)
                .allowedMethod(HttpMethod.DELETE)
                .allowedMethod(HttpMethod.PATCH)
                .allowedMethod(HttpMethod.OPTIONS)
                .allowedHeader("Access-Control-Allow-Methods")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Access-Control-Allow-Credentials")
                .allowedHeader("Access-Control-Allow-Headers")
                .allowedHeader("Content-Type")
                .allowedHeader("Authorization")
                .allowedHeader("Cache-Control")
                .allowedHeader("X-Requested-With")
                .allowedHeader("Accept")
                .allowedHeader("Origin")
        );

        configureRoutes(router);
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(Integer.parseInt(applicationConfig.getStringProperty("rest_expose_port","8081")));
        startPromise.complete();
    }

    public void setRequestHandlerList(List<RequestHandler> requestHandlerList) {
        this.requestHandlerList = requestHandlerList;
    }
}
