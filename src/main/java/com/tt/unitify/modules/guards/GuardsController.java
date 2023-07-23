package com.tt.unitify.modules.guards;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@Log4j2
@RestController
@RequestMapping("/guard")
public class GuardsController {
    @Autowired
    private GuardsService guardsService;
    @PostMapping()
    public String createUser(@RequestBody GuardEntity guard) throws ExecutionException, InterruptedException {
        log.info("Guard: {}", guard);
        return guardsService.createGuard(guard);
    }
}
