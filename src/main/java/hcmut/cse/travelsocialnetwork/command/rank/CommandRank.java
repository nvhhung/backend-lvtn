package hcmut.cse.travelsocialnetwork.command.rank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : hung.nguyen23
 * @since : 9/22/22 Thursday
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommandRank {
    private String userId;
    private String postId;
    private int page;
    private int size;
}
