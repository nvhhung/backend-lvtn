package hcmut.cse.travelsocialnetwork.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import eu.dozd.mongo.annotation.Embedded;
import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;
import hcmut.cse.travelsocialnetwork.factory.repo.PO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Post extends PO {
    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    private String id;

    private String userPost;
    private String content;
    private String link;
    private Integer status;
    private List<Url> urls;
    private Integer likeSize;
    private Integer commentSize;
    private List<String> blackList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embedded
    @Builder
    public static class Url implements Serializable {
        private String url;
        private String id;
        private String type;//video, image
        private Long createdDate;
    }

}
