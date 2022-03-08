package ddd.post;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import eu.dozd.mongo.annotation.Entity;
import eu.dozd.mongo.annotation.Id;
import factory.repo.PO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Post extends PO {
    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    ObjectId _id;
    @Builder.Default
    private Boolean is_deleted = false;
    private String user_id;
}
