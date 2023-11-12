package com.tt.unitify.modules.post;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@Log4j2
@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    PostService postService;

    @PostMapping("/create-alert")
    public String createAlert() throws ExecutionException, InterruptedException {
        postService.sendAlertNotification();

        return "createAlert";
    }
}
