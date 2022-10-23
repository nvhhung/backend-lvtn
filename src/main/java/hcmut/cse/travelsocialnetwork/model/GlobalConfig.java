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
import org.bson.types.ObjectId;

/**
 * @author : hung.nguyen23
 * @since : 10/23/22 Sunday
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class GlobalConfig extends PO {
    @JsonSerialize(using = ToStringSerializer.class)
    @Id
    private ObjectId _id;

    private String key;
    private String value;
}
