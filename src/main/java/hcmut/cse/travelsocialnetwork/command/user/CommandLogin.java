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
public class CommandLogin {
    private String username;
    private String password;
    private Boolean isAdmin;
}
