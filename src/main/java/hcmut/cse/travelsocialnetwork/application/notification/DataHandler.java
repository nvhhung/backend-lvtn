package hcmut.cse.travelsocialnetwork.application.notification;

import hcmut.cse.travelsocialnetwork.application.media.MediaApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @author : hung.nguyen23
 * @since : 11/26/22 Saturday
 **/
public class DataHandler extends TextWebSocketHandler {
    private static final Logger log = LogManager.getLogger(MediaApplication.class);


    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("Message: {}", message.getPayload());

        session.sendMessage(new TextMessage("Hello UI friends"));
    }
}
