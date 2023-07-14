package com.tt.unitify.modules.users;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
@Log4j2
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/create")
    public String createUser(@RequestBody UserDto user) throws ExecutionException, InterruptedException {
        log.info("User: {}", user);
        return userService.createUser(user);
    }

}
