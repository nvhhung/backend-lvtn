package hcmut.cse.travelsocialnetwork.command.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : hung.nguyen23
 * @since : 9/7/22 Wednesday
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommandComment {
    private String commentId;
    private String userId;
    private String postId;
    private String content;
    private Integer page;
    private Integer size;
}
