package hcmut.cse.travelsocialnetwork.model;

import eu.dozd.mongo.annotation.Embedded;
import eu.dozd.mongo.annotation.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Document(collection = "post")
public class Post {
    @Id
    private String _id;
    private Long created_date;
    private Long last_updated_date;
    private Integer status;

    private String user_post;
    private String content;
    private String link;
    private List<User> black_list_user;

    private List<Url> urls;
    private Integer like_size;
    private Integer comment_size;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embedded
    @Builder
    public static class Url implements Serializable {
        private String url;
        private String _id;
        private String type;//video, image
        private Long created_date;
    }

}
