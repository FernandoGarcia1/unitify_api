package com.tt.unitify.job;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@EnableScheduling
@Log4j2
public class Job {
    //@Scheduled(cron = "* * * * * *")
    private void job(){
        Date date = new Date();

        log.info("Job is running : {}", date.getTime());
    }
}
