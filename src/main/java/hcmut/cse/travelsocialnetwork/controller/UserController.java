package hcmut.cse.travelsocialnetwork.controller;

import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private static final Logger LOGGER = LogManager.getLogger();
    @GetMapping("/user/")
    public String testConnect() {
        return  "User successful";
    }

    @PostMapping("/user/add-user")
    public String addUser(@RequestBody String bodyData) {
        var bodyDataJson = new JsonObject(bodyData);
        var name = bodyDataJson.getString("name");
        LOGGER.info(bodyDataJson);
        return "test add";
    }
}
