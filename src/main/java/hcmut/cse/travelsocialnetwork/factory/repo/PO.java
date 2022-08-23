package hcmut.cse.travelsocialnetwork.factory.repo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : hung.nguyen23
 * @since : 8/1/22 Monday
 **/
@Data
@NoArgsConstructor
public abstract class PO {
    private Long createTime;
    private Long lastUpdateTime;
    private Boolean isDeleted;
}
