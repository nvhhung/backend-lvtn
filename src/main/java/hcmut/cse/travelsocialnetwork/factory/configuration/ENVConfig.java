package hcmut.cse.travelsocialnetwork.factory.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author : hung.nguyen23
 * @since : 8/1/22 Monday
 **/
@Component
public class ENVConfig {
    @Autowired
    private Environment env;

    public String getStringProperty(String key) {
        return Optional.ofNullable(env.getProperty(key)).orElse(key);
    }

    public String getStringProperty(String key, String defaultValue) {
        return env.getProperty(key, defaultValue);
    }
}
