package hcmut.cse.travelsocialnetwork.command.rate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : hung.nguyen23
 * @since : 9/21/22 Wednesday
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommandRate {
    private String userId;
    private String postId;
    private Integer point;
    private int page;
    private int size;
}
