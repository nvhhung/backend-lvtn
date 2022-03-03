package utils.mongodb.v2;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@ToString
public class KijiKokoroMongoDBConfig {
    private String connectionURL;
    private String databaseName;
}
