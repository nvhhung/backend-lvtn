package utils.vertx;

import io.vertx.core.Vertx;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data

@Component
public class VertxProvider {
    private Vertx vertx;

    @Autowired
    public VertxProvider() {
        vertx = Vertx.vertx();
    }
}
