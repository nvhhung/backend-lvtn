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
 * @since : 9/12/22 Monday
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Notification extends PO {
    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    private ObjectId _id;

    private Boolean isRead;
    private String userId;
    private String title;
    private String content;
    private String objectId;
    private String image;
    private String type; // like, comment, tag

    private InfoUser infoUser;
    private List<String> registrationTokens;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embedded
    @Builder
    public static class InfoUser implements Serializable {
        private String userId;
        private String fullName;
        private String avatar;
    }
}
