package ddd.user.application;

import ddd.user.port_adapter.IUserRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class UserApplication implements IUserApplication{
    @Autowired
    private IUserRepository userRepository;

    private static final Logger LOGGER = LogManager.getLogger();
}
