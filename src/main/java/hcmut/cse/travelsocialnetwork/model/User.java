package hcmut.cse.travelsocialnetwork.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;
import hcmut.cse.travelsocialnetwork.factory.repo.PO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User extends PO {
    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    ObjectId id;
    // information login
    private String username;
    private String password;
    private Boolean isAdmin;
    // personal
    private String fullName;
    private String phone;
    private String email;
    private String birthday;
    private String avatar;
    private String cover;
    private String address;
    // feature
    private List<String> userFollow;
    private Long experiencePoint;
    private Integer level;
    private String status;
    // provider

}
