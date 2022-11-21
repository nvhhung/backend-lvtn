package hcmut.cse.travelsocialnetwork.application.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import hcmut.cse.travelsocialnetwork.application.comment.CommentApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author : hung.nguyen23
 * @since : 11/21/22 Monday
 **/
@Component
public class FCMService {
    private static final Logger log = LogManager.getLogger(CommentApplication.class);

//    public void sendMessageToToken(PushNotificationRequest request) {
//        var message = getPreconfigureMesssageToToken(request);
//        var json = JSONUtils.objToJsonObj(message);
//        var response = send
//    }
//
    private String sendAndGetResponse(Message message) {
        try{
            return FirebaseMessaging.getInstance().sendAsync(message).get();
        } catch (Exception e) {
            log.error(e.getMessage());
            return "";
        }
    }
}
