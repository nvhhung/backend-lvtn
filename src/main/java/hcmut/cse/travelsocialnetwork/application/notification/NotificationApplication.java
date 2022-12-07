package hcmut.cse.travelsocialnetwork.application.notification;

import hcmut.cse.travelsocialnetwork.command.notification.CommandNotification;
import hcmut.cse.travelsocialnetwork.factory.configuration.ENVConfig;
import hcmut.cse.travelsocialnetwork.model.Notification;
import hcmut.cse.travelsocialnetwork.model.Paginated;
import hcmut.cse.travelsocialnetwork.repository.notification.INotificationRepository;
import hcmut.cse.travelsocialnetwork.utils.Constant;
import hcmut.cse.travelsocialnetwork.utils.CustomException;
import hcmut.cse.travelsocialnetwork.utils.JSONUtils;
import io.ably.lib.rest.AblyRest;
import io.ably.lib.types.AblyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;


/**
 * @author : hung.nguyen23
 * @since : 9/12/22 Monday
 **/
@Component
public class NotificationApplication implements INotificationApplication {
    private static final Logger log = LogManager.getLogger(NotificationApplication.class);
    INotificationRepository notificationRepository;
    private final AblyRest ablyRest;

    public NotificationApplication(INotificationRepository notificationRepository,
                                   ENVConfig applicationConfig) throws AblyException {
        this.notificationRepository = notificationRepository;

        ablyRest = new AblyRest(applicationConfig.getStringProperty("application.ably_api_key", ""));
        log.info("create ablyRest success");
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
        var notifyAdd = notificationRepository.add(notificationNew);
        if (notifyAdd.isPresent()) {
            log.info("push message to channel {}", commandNotification.getChannel());
            processingNotify(commandNotification , notifyAdd.get());
        }
        return notifyAdd;
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
    public Optional<Object> loadNotification(CommandNotification commandNotification) {
        var query = new Document(Constant.FIELD_QUERY.USER_ID,commandNotification.getUserId());
        var sort = new Document(Constant.FIELD_QUERY.CREATE_TIME, -1);
        var notifyList = notificationRepository.search(query, sort, commandNotification.getPage(), commandNotification.getSize());
        var totalItem = notificationRepository.count(query);
        log.info(String.format("user %s have notification size %d", commandNotification.getUserId(), totalItem.orElse(0L)));
        return Optional.of(new Paginated<>(notifyList.orElse(new ArrayList<>()), commandNotification.getPage(), commandNotification.getSize(), totalItem.orElse(0L)));
    }

    public void processingNotify(CommandNotification commandNotification, Notification notification) throws AblyException {
        var channel = ablyRest.channels.get(commandNotification.getChannel());
        channel.publish("realtime", JSONUtils.objToJsonString(notification));
    }
}
