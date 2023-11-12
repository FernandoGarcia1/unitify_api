package com.tt.unitify.modules.notifications;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@Log4j2
@RestController
@RequestMapping
public class NotificationsController {

    @Autowired
    NotificationsService notificationsService;

    @PostMapping("/create-alert")
    public String createAlert() throws ExecutionException, InterruptedException, FirebaseMessagingException {

        log.info("createAlert");
        notificationsService.sendAlertMulticast();
        return "createAlert";
    }

    @PostMapping("/notification-new-account")
    public String notificationNewAccount() throws ExecutionException, InterruptedException, FirebaseMessagingException {

        log.info("notificationNewAccount");
        notificationsService.sendNotificationNewAccount();
        return "notificationNewAccount";
    }
}
