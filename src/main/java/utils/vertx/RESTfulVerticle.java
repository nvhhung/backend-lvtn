package utils.vertx;

import configuration.ENVConfig;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.ResponseTimeHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RESTfulVerticle extends AbstractVerticle {
    private static Logger LOGGER = LogManager.getLogger();
    private List<RequestHandler> requestHandlers = new ArrayList<>();
    @Autowired
    protected ENVConfig applicationConfig;
//    @Autowired
//    private VIHATSaaSJWTAuthHandler vihatSaaSJWTAuthHandler;

    private void configureRoutes(Router router) {
        //VIHATSaaSJWTAuthHandler vihatSaaSJWTAuthHandler = new VIHATSaaSJWTAuthHandler(jwtAuthProvider.getJwtAuth());
       // VIHATSaaSBasicAuthHandler vihatSaaSBasicAuthHandler = new VIHATSaaSBasicAuthHandler(null);
        this.requestHandlers.forEach(requestHandler -> {
            if(requestHandler.isEnableAuthentication()){
//                if(requestHandler.getAuthType() == 1) {
//                    router.route(requestHandler.getPath()).handler(vihatSaaSJWTAuthHandler).handler(requestHandler.handle());
//                } else { // = 2
//                    router.route(requestHandler.getPath()).handler(vihatSaaSBasicAuthHandler).handler(requestHandler.handle());
//                }
            } else {
                router.route(requestHandler.getMethod(), requestHandler.getPath()).handler(requestHandler.handle());
            }
            LOGGER.info("Configuring route {}:{}", requestHandler.getMethod(), requestHandler.getPath());
        });
    }

    @Override
    public void start(Future<Void> startFuture) {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create()
                .setDeleteUploadedFilesOnEnd(true)
                .setBodyLimit(5 * 1024 * 1024) // 5 MB
                .setUploadsDirectory(applicationConfig.getStringProperty("application.upload.directory", "/root/tmp/")));
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
                .allowedHeader("Origin"));

        router.route().handler(ResponseTimeHandler.create());


        configureRoutes(router);
        vertx.createHttpServer().requestHandler(router).listen(Integer.parseInt(applicationConfig.getStringProperty("rest_expose_port", "8080")));
        startFuture.succeeded();
    }

    public void setRequestHandlers(List<RequestHandler> requestHandlers) {
        this.requestHandlers = requestHandlers;
    }
}
