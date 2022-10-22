package hcmut.cse.travelsocialnetwork.command.media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : hung.nguyen23
 * @since : 10/19/22 Wednesday
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommandMedia {
    private String postId;
    private String link;
    private String type;
    private String userId;
    private int page;
    private int size;
}
