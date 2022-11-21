package hcmut.cse.travelsocialnetwork.application.notification;

import hcmut.cse.travelsocialnetwork.service.firebase.FirebaseConfigure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author : hung.nguyen23
 * @since : 9/12/22 Monday
 **/
@Component
public class NotificationApplication implements INotificationApplication {
    @Autowired
    private Environment env;

    @PostConstruct
    public void initialize() {

    }
}
