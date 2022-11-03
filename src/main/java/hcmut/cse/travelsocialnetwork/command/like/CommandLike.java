package hcmut.cse.travelsocialnetwork.command.like;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : hung.nguyen23
 * @since : 9/20/22 Tuesday
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommandLike {
    private String postId;
    private String userId;
    private int page;
    private int size;
}
