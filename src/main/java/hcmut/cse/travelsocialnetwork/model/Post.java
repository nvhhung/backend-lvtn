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
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

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
    private ObjectId _id;

    private String userId;
    private String title;
    private String content;
    private String type;
    private String link;
    private String status; // onlyMe, public, follow, unknown
    private List<Url> urls;
    private Integer likeSize;
    private Integer commentSize;
    private Long point;
    private List<String> blackListUsers;

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
