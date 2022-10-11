package hcmut.cse.travelsocialnetwork.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import eu.dozd.mongo.annotation.Embedded;
import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;
import hcmut.cse.travelsocialnetwork.factory.repo.PO;
import io.vertx.core.json.JsonArray;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private String destination;
    private String type;
    private String status; // onlyMe, public, follow, unknown
    private String media;
    private Integer likeSize;
    private Integer commentSize;
    private Integer rateSize;
    private Integer point;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embedded
    @Builder
    public static class Media implements Serializable {
        private String link;
        private String type;//video, image
    }

}
