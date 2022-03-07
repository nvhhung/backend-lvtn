package configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ENVConfig {
    @Autowired
    private Environment env;

    public String getStringProperty(String key) {
        String result = env.getProperty(key);
        return result == null ? key : result;
    }

    public String getStringProperty(String key, String defaultValue) {
        return env.getProperty(key, defaultValue);
    }
}
