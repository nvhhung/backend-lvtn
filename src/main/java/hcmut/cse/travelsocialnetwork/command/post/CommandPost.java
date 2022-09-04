package hcmut.cse.travelsocialnetwork.command.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : hung.nguyen23
 * @since : 8/31/22 Wednesday
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommandPost {
    private String id;
    private String userId;
    private String title;
    private String content;
    private String link;
    private String type;
    private String video;
    private String status;
    private List<String> blackListUsers;
}
