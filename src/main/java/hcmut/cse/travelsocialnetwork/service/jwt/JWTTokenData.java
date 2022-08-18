package hcmut.cse.travelsocialnetwork.service.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : hung.nguyen23
 * @since : 8/19/22 Friday
 **/
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class JWTTokenData {
    String userId;
    String type;
    Boolean loggedExternal;
}
