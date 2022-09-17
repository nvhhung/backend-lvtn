package hcmut.cse.travelsocialnetwork.command.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : hung.nguyen23
 * @since : 9/17/22 Saturday
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommandUser {
    private String userId;
    private String username;
    private String password;
    private String fullName;
    private String phone;
    private String email;
    private String birthday;
    private String avatar;
    private String cover;
    private String address;
    private List<String> userFollow;
    private Long experiencePoint;
    private Integer level;
    private String status;

}
