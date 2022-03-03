package utils.mongodb.build_query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class NestedElement {
    String parent;
    String field;
}