package hcmut.cse.travelsocialnetwork.command.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : hung.nguyen23
 * @since : 8/31/22 Wednesday
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommandPost {
    private String id;;
    private String postId;
    private String userPost;
    private String userId;
}
