package hcmut.cse.travelsocialnetwork.service.vertx.authen;

import io.vertx.core.Vertx;
import io.vertx.ext.auth.jwt.JWTAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : hung.nguyen23
 * @since : 8/15/22 Monday
 **/
@Component
public class JWTAuthProvider {
    JWTAuth jwtAuth;

    @Autowired
    public  JWTAuthProvider(Vertx vertx, )
}
