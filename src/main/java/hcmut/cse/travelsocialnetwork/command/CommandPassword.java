package hcmut.cse.travelsocialnetwork.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : hung.nguyen23
 * @since : 8/22/22 Monday
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommandPassword {
    private String userName;
    private String oldPassword;
    private String newPassword;
    private String kind;
}
