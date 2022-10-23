package hcmut.cse.travelsocialnetwork.command.globalconfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : hung.nguyen23
 * @since : 10/23/22 Sunday
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommandGlobalConfig {
    private String key;
    private String value;
    private int page;
    private int size;
}
