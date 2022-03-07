package utils.vertx;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

public interface RequestHandler {
    HttpMethod getMethod();

    String getPath();

    Handler<RoutingContext> handle();

    Boolean isEnableAuthentication();

    int getAuthType(); // 1: jwt, 2: bacic auth

    static RequestHandler init(HttpMethod httpMethod, String path, Handler<RoutingContext> handler, Boolean isEnableAuthentication, int... arguments) {
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
