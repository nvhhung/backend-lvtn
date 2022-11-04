package hcmut.cse.travelsocialnetwork.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : hung.nguyen23
 * @since : 8/16/22 Tuesday
 **/
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LoginToken {
    private String accessToken;
    private String refreshToken;
    private String userId;
}
