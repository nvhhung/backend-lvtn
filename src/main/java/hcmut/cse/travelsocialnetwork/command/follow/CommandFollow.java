package hcmut.cse.travelsocialnetwork.command.follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : hung.nguyen23
 * @since : 9/17/22 Saturday
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommandFollow {
    private String userId;
    private String userIdTarget;
}
