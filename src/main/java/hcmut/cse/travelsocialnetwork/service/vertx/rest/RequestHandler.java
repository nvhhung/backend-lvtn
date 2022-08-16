package hcmut.cse.travelsocialnetwork.service.vertx.rest;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

/**
 * @author : hung.nguyen23
 * @since : 8/15/22 Monday
 **/
public interface RequestHandler {
    HttpMethod getMethod();
    String getPath();
    Handler<RoutingContext> handle();
    Boolean isEnableAuthentication();
    int getAuthType();

    static RequestHandler init(HttpMethod httpMethod, String path, Handler<RoutingContext> handler,Boolean isEnableAuthentication, int... arguments) {
        return new RequestHandler() {
            @Override
            public HttpMethod getMethod() {
                return httpMethod;
            }

            @Override
            public String getPath() {
                return path;
            }

            @Override
            public Handler<RoutingContext> handle() {
                return handler;
            }

            @Override
            public Boolean isEnableAuthentication() {
                return isEnableAuthentication;
            }

            @Override
            public int getAuthType() {
                if(arguments != null && arguments.length >= 1) {
                    return  arguments[0];
                }
                return 1;
            }
        };
    }

    static RequestHandler init(HttpMethod httpMethod, String path, Handler<RoutingContext> handler) {
        return new RequestHandler() {
            @Override
            public HttpMethod getMethod() {
                return httpMethod;
            }

            @Override
            public String getPath() {
                return path;
            }

            @Override
            public Handler<RoutingContext> handle() {
                return handler;
            }

            @Override
            public Boolean isEnableAuthentication() {
                return true;
            }

            public int getAuthType() {
                return 1;
            }
        };
    }
}
