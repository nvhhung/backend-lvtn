package hcmut.cse.travelsocialnetwork.application.notification;

import hcmut.cse.travelsocialnetwork.command.notification.CommandNotification;
import hcmut.cse.travelsocialnetwork.model.Notification;

import java.util.List;
import java.util.Optional;

/**
 * @author : hung.nguyen23
 * @since : 9/12/22 Monday
 **/
public interface INotificationApplication {
    Optional<Notification> createNotification(CommandNotification commandNotification) throws Exception;
    Optional<Boolean> readNotification(CommandNotification commandNotification) throws Exception;
    Optional<List<Notification>> loadNotification(CommandNotification commandNotification) throws Exception;
}
