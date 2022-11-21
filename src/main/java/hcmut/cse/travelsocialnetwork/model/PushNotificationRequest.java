package hcmut.cse.travelsocialnetwork.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : hung.nguyen23
 * @since : 11/21/22 Monday
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PushNotificationRequest {
    private String title;
    private String message;
    private String topic;
    private String token;
}
