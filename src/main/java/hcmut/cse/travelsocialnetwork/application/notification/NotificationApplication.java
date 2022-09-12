package hcmut.cse.travelsocialnetwork.application.notification;

import com.google.firebase.messaging.*;
import hcmut.cse.travelsocialnetwork.model.Notification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : hung.nguyen23
 * @since : 9/12/22 Monday
 **/
@Component
public class NotificationApplication implements INotificationApplication{
    private static final Logger log = LogManager.getLogger(NotificationApplication.class);

    private final FirebaseMessaging firebaseMessaging;

    public NotificationApplication(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    public BatchResponse sendNotification(Notification notification) {
        var registerTokens = notification.getRegistrationTokens();
        var message = MulticastMessage.builder()
                .addAllTokens(registerTokens)
                .build();

        BatchResponse batchResponse = null;
        try{
            batchResponse = firebaseMessaging.sendMulticast(message);
        } catch (FirebaseMessagingException e) {
            log.info("Firebase error: " + e.getMessage());
        }

        if (batchResponse.getFailureCount() > 0) {
            List<SendResponse> responses = batchResponse.getResponses();
            var failedTokens = new ArrayList<>();
        }
        return batchResponse;
    }
}
