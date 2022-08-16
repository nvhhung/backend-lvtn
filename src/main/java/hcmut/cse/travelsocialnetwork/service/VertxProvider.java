package hcmut.cse.travelsocialnetwork.service;

import io.vertx.core.Vertx;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : hung.nguyen23
 * @since : 8/15/22 Monday
 **/
@Component
@Data
public class VertxProvider {
    private Vertx vertx;

    @Autowired
    public VertxProvider() {
        vertx = Vertx.vertx();
    }
}
