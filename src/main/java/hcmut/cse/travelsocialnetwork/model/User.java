package hcmut.cse.travelsocialnetwork.model;

import eu.dozd.mongo.annotation.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Document(collection = "user")
public class User {
    @Id
    private String _id;
    private Long created_date;
    private Long last_updated_date;
    private int status;
    // personal
    private String name;
    private String phone;
    private String email;
    private String birthday;
    private String avatar;
    private String cover;
    private String address;
    // feature
    private List<String> userFollow;
}
