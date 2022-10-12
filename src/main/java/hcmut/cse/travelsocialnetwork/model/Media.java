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
 * @since : 10/12/22 Wednesday
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Media extends PO {
  @JsonSerialize(using = ToStringSerializer.class)
  @Id
  private ObjectId _id;

  private String postId;
  private String commentId;
  private String type; // video or image
  private String link;
}
