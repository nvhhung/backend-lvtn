package hcmut.cse.travelsocialnetwork.service.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * @author : hung.nguyen23
 * @since : 9/9/22 Friday
 **/
@Component
public class FirebaseConfigure {
    private static final Logger log = LogManager.getLogger(FirebaseConfigure.class);

    public FirebaseConfigure() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
                new ClassPathResource("firebaseServiceAccountKey.json")
                        .getInputStream());

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(googleCredentials)
                .build();
        FirebaseApp app = FirebaseApp.initializeApp(options, "travel-social-network");
        log.info("deploy firebase success");
    }
}
