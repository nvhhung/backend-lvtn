package hcmut.cse.travelsocialnetwork.application.notification;

import hcmut.cse.travelsocialnetwork.application.comment.CommentApplication;
import hcmut.cse.travelsocialnetwork.model.PushNotificationRequest;
import hcmut.cse.travelsocialnetwork.utils.JSONUtils;
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
//    private String sendAndGetResponse(Message message)
}
