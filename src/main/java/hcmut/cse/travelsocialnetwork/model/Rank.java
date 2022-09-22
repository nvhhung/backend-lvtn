package hcmut.cse.travelsocialnetwork.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : hung.nguyen23
 * @since : 9/22/22 Thursday
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rank {
    private String userId;
    private Integer point;
    private Integer position;
}
