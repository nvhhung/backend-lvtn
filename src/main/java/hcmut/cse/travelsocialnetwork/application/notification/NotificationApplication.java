package hcmut.cse.travelsocialnetwork.application.notification;

import hcmut.cse.travelsocialnetwork.command.notification.CommandNotification;
import hcmut.cse.travelsocialnetwork.model.Notification;
import hcmut.cse.travelsocialnetwork.repository.notification.INotificationRepository;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.CustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * @author : hung.nguyen23
 * @since : 9/12/22 Monday
 **/
@Component
public class NotificationApplication implements INotificationApplication {
    private static final Logger log = LogManager.getLogger(NotificationApplication.class);
    INotificationRepository notificationRepository;

    public NotificationApplication(INotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }


    @Override
    public Optional<Notification> createNotification(CommandNotification commandNotification) throws Exception {
        var notificationNew = Notification.builder()
            .userId(commandNotification.getUserId())
            .isRead(false)
            .objectId(commandNotification.getObjectId())
            .userIdTrigger(commandNotification.getUserIdTrigger())
            .content(commandNotification.getContent())
            .title(commandNotification.getTitle())
            .type(commandNotification.getType())
            .build();
        return notificationRepository.add(notificationNew);
    }

    @Override
    public Optional<Boolean> readNotification(CommandNotification commandNotification) throws Exception {
        var notification = notificationRepository.getById(commandNotification.get_id());
        if (notification.isEmpty()) {
            log.warn(String.format("%s not found notification", commandNotification.getUserId()));
            throw new CustomException(Constant.ERROR_MSG.NOT_FOUND_NOTIFICATION);
        }
        notification.get().setIsRead(true);
        var notifyUpdate = notificationRepository.update(notification.get().get_id().toString(), notification.get());
        return Optional.of(notifyUpdate.isPresent());
    }

    @Override
    public Optional<List<Notification>> loadNotification(CommandNotification commandNotification) throws Exception {
        var query = new Document(Constant.FIELD_QUERY.USER_ID,commandNotification.getUserId());
        var sort = new Document(Constant.FIELD_QUERY.CREATE_TIME, -1);
        var notifyList = notificationRepository.search(query, sort, commandNotification.getPage(), commandNotification.getSize());
        if (notifyList.isEmpty()) {
            log.warn(String.format("%s no have notify", commandNotification.getUserId()));
            return Optional.of(new ArrayList<>());
        }
        return notifyList;
    }
}
