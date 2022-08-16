package hcmut.cse.travelsocialnetwork.service.vertx.rest;

import hcmut.cse.travelsocialnetwork.service.jwt.JWTAuthProvider;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.AuthenticationHandlerImpl;
import org.springframework.stereotype.Component;

/**
 * @author : hung.nguyen23
 * @since : 8/15/22 Monday
 **/
@Component
public class JWTAuthHandler extends AuthenticationHandlerImpl {

    public JWTAuthHandler(JWTAuthProvider authProvider) {
        super(authProvider.getJwtAuth());
    }

    @Override
    public void handle(RoutingContext routingContext) {
        var request = routingContext.request();
        request.pause();
        var authorization = request.headers().get(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            routingContext.fail(401);
        } else {
            var parts = authorization.split(" ");
            if (parts.length != 2) {
                routingContext.fail(401);
            } else {
                var schema = parts[0];
                if (!"bearer".equalsIgnoreCase(schema)) {
                    routingContext.fail(401);
                } else {
                    var token = parts[1];
                    var credentials = new JsonObject();
                    credentials.put("jwt", token);
                    authProvider.authenticate(credentials, res -> {
                        if (res.succeeded()) {
                            var jsonObject = res.result().principal();
                            routingContext.setUser(res.result());
                        } else {
                            routingContext.fail(401);
                        }
                    });
                }
            }

        }
    }

    @Override
    public void authenticate(RoutingContext context, Handler<AsyncResult<User>> handler) {
    }

    @Override
    public void postAuthentication(RoutingContext ctx) {
        super.postAuthentication(ctx);
    }
}