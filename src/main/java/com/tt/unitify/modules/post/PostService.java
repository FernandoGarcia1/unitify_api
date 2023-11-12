package com.tt.unitify.modules.post;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import java.util.List;
import com.tt.unitify.modules.users.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import java.util.concurrent.ExecutionException;


@Log4j2
@Service
public class PostService {

    public static final String COLLECTION="POST";
    private final Firestore db = FirestoreClient.getFirestore();

    @Autowired
    UserService userService;

    public void sendAlertNotification() throws ExecutionException, InterruptedException {
        log.info("sendAlert");
        List<String> idUsers = userService.getUsersWithTokenFmc();
        log.info("idUsers: {}", idUsers);
        /*String title = "Alerta de seguridad";
        String body = "Se ha detectado un intruso en el edificio";
        String userFcmToken = "cDtJ0YC8T-Ooo3B7WzQet4:APA91bHkhJCjMPFjCDkXWolepIIpHw_1iWbvGddM5QpivLqOLc_7pVhmUrSX9pJyzmP11JhqhMWXCHCJ_ykNkRjKN06VQal-qsqum-vzRMGP5IipXXDJUWyiH9KbxLfLuHgoJNx1gk1M";
        Notification.Builder notificationBuilder = Notification.builder()
                .setTitle(title)
                .setBody(body);
        Message message = Message.builder()
                .setNotification(notificationBuilder.build())
                .setToken(userFcmToken)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Notificación enviada con éxito: " + response);
        } catch (Exception e) {
            System.err.println("Error al enviar la notificación: " + e.getMessage());
        }*/
    }
}
