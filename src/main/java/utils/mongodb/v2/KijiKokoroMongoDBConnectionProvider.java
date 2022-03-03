package utils.mongodb.v2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KijiKokoroMongoDBConnectionProvider {
    private Map<String, KijiKokoroMongoDBConnectionProvider> connections = new ConcurrentHashMap<>();

}
