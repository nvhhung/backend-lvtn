package hcmut.cse.travelsocialnetwork.service.socket;

import hcmut.cse.travelsocialnetwork.application.notification.DataHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author : hung.nguyen23
 * @since : 8/31/22 Wednesday
 **/
@Configuration
@EnableWebSocket
public class WebSocketConfigure implements  WebSocketConfigurer{
    /**
     * http://localhost:8080/data
     * @param registry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(getDataHandler(), "/data");
        registry.addHandler(getDataHandler(), "/haha");
    }


    @Bean
    DataHandler getDataHandler() {
        return new DataHandler();
    }
}
