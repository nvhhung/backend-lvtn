package hcmut.cse.travelsocialnetwork.command.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : hung.nguyen23
 * @since : 11/26/22 Saturday
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommandNotification {
    private String _id;

    private Boolean isRead;
    private String userId;
    private String userIdTrigger;
    private String title;
    private String content;
    private String objectId;
    private String type; // like, comment, rate
    private String channel;
    private int page;
    private int size;
}
