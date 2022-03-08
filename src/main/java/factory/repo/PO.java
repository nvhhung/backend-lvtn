package factory.repo;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public abstract class PO {
    private Long created_date;
    private Long last_updated_date;
}
