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
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.List;

/**
 * @author : hung.nguyen23
 * @since : 9/7/22 Wednesday
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Comment extends PO {
    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    private ObjectId _id;

    private String userId;
    private String postId;
    private String content;
    private String status;
    private List<String> images;
    private List<TagUser> tagUsers;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embedded
    @Builder
    public static class TagUser implements Serializable {
        String name;
        String id;
    }

}