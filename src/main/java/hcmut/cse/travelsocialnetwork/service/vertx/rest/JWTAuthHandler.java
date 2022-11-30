package hcmut.cse.travelsocialnetwork.service.vertx.rest;

import hcmut.cse.travelsocialnetwork.service.jwt.JWTAuthProvider;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.AuthenticationHandlerImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author : hung.nguyen23
 * @since : 8/15/22 Monday
 **/
@Component
public class JWTAuthHandler extends AuthenticationHandlerImpl {

    private static final Logger log = LogManager.getLogger(JWTAuthHandler.class);

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
                    credentials.put("token", token);
                    authProvider.authenticate(credentials, res -> {
                        if (res.succeeded()) {
                            var user = res.result();
                            if (checkExpired(user.attributes())) {
                                log.warn("token expired");
                                routingContext.fail(401);
                                return;
                            }
                            routingContext.setUser(res.result());
                            request.resume();
                            routingContext.next();
                        } else {
                            log.info("token invalid because: " + res.cause());
                            routingContext.fail(401);
                        }
                    });
                }
            }

        }
    }

    private boolean checkExpired(JsonObject parseToken) {
        var exp = parseToken.getString("exp", "");
        var expiredTime = Long.parseLong(exp);
        if (System.currentTimeMillis() > expiredTime) {
            return true;
        }
        return false;
    }

    @Override
    public void authenticate(RoutingContext context, Handler<AsyncResult<User>> handler) {
    }

    @Override
    public void postAuthentication(RoutingContext ctx) {
        super.postAuthentication(ctx);
    }
}
