package hcmut.cse.travelsocialnetwork.command.post;

import hcmut.cse.travelsocialnetwork.model.Media;
import hcmut.cse.travelsocialnetwork.model.Post;
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
    private String destination;
    private List<Media> mediaList;
    private String status;
    private Integer page;
    private Integer size;
}
