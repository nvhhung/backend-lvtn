package hcmut.cse.travelsocialnetwork.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/user/")
    public String testConnect() {
        return  "User successful";
    }

    @PostMapping("/user/add-user")
    public String addUser(@RequestBody String bodyData) {

        return "test add";
    }
}
