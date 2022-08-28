package hcmut.cse.travelsocialnetwork.command.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : hung.nguyen23
 * @since : 8/16/22 Tuesday
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommandRegister {
    private String username;
    private String password;
    private String kind;
    private String phone;
    private String name;
    private String avatar;
    private String role;
}
