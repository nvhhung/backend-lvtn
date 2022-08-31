package hcmut.cse.travelsocialnetwork.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * @author : hung.nguyen23
 * @since : 8/31/22 Wednesday
 **/
@Component
public class MessageController {

    @MessageMapping
    @SendTo("/topic/messages")
    public String send(String userName) {
        return "Hello, " + userName;
    }
}
