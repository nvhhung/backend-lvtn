package hcmut.cse.travelsocialnetwork.controller;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.HashMap;

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

    public String pushNotification() throws FirebaseMessagingException {
        Message message = Message.builder()
                .putAllData(new HashMap<>())
                .build();
        var s = "123";
        String response = null;
        response = FirebaseMessaging.getInstance().send(message);
        return response;
    }
}
