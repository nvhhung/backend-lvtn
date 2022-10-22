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
    private List<Media> mediaList;
    private Integer likeSize;
    private Integer commentSize;
    private Integer rateSize;
    private Integer point;

    public Post cloneFull() {
        var post =  new Post(this._id, this.userId, this.title, this.content, this.destination,this.type,this.status, this.mediaList, this.likeSize, this.commentSize, this.rateSize, this.point);
        post.setLastUpdateTime(this.getLastUpdateTime());
        post.setCreateTime(this.getCreateTime());
        return post;
    }
}
