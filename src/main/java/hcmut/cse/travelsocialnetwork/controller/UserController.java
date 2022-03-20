package hcmut.cse.travelsocialnetwork.controller;

import hcmut.cse.travelsocialnetwork.application.user.IUserApplication;
import hcmut.cse.travelsocialnetwork.factory.AbstractController;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController extends AbstractController {
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    IUserApplication userApplication;

    @GetMapping("/user/")
    public String testConnect() {
        return  "User successful";
    }

    @PostMapping("/user/add-user")
    public String addUser(@RequestBody String bodyData) {
        var bodyDataJson = new JsonObject(bodyData);
        var name = bodyDataJson.getString("name");
        LOGGER.info(bodyDataJson);
        return outputJson(9999,userApplication.addUser(name).orElse(null));
    }

    @GetMapping("/user/find-user")
    public String findUser(@RequestParam String _id) {
        return outputJson(9999,userApplication.findUser(_id).orElse(null));
    }

}
