package hcmut.cse.travelsocialnetwork.service.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;

/**
 * @author : hung.nguyen23
 * @since : 9/9/22 Friday
 **/
@Configuration
@RequiredArgsConstructor
public class FirebaseConfigure {
    private static final Logger log = LogManager.getLogger(FirebaseConfigure.class);

    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
                new ClassPathResource("firebaseServiceAccountKey.json")
                        .getInputStream());

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build();
        FirebaseApp app = FirebaseApp.initializeApp(options, "travel-social-network");
        log.info("deploy firebase success");
        return FirebaseMessaging.getInstance(app);
    }
}
