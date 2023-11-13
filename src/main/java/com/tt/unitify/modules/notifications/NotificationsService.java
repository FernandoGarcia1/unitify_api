package com.tt.unitify.modules.notifications;

import com.google.firebase.messaging.*;
import com.tt.unitify.modules.users.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Log4j2
public class NotificationsService {

    @Autowired
    UserService userService;

    public void sendAlertNotification() throws ExecutionException, InterruptedException {
        log.info("sendAlert");
        List<String> idUsers = userService.getUsersWithTokenFmc();
        log.info("idUsers: {}", idUsers);
        String title = "Alerta de seguridad";
        String body = "Se ha detectado un intruso en el edificio";
        String userFcmToken = "cDtJ0YC8T-Ooo3B7WzQet4:APA91bHkhJCjMPFjCDkXWolepIIpHw_1iWbvGddM5QpivLqOLc_7pVhmUrSX9pJyzmP11JhqhMWXCHCJ_ykNkRjKN06VQal-qsqum-vzRMGP5IipXXDJUWyiH9KbxLfLuHgoJNx1gk1M";
        Notification.Builder notificationBuilder = Notification.builder()
                .setTitle(title)
                .setBody(body);
        Message message = Message.builder()
                .setNotification(notificationBuilder.build())
                .putData("title", title)
                .putData("body", body)
                .putData("click_action", "FLUTTER_NOTIFICATION_CLICK")
                .putData("id", "1")//1 es alerta
                .setToken(userFcmToken)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Notificación enviada con éxito: " + response);
        } catch (Exception e) {
            log.info("Error al enviar la notificación: " + e.getMessage());
        }
    }

    public void sendAlertMulticast(String tokenFCM) throws ExecutionException, InterruptedException, FirebaseMessagingException {
        List<String> tokenFmcList = userService.getUsersWithTokenFmc();
        if (!(tokenFCM == null || tokenFCM.isEmpty() || tokenFCM.isBlank() || tokenFCM.equals(""))) {
            tokenFmcList.remove(tokenFCM);
        }
        MulticastMessage message = MulticastMessage.builder()
                .setNotification(Notification.builder()
                        .setTitle("¡Alerta!")
                        .setBody("Un usuario ha agregado una nueva alerta")
                        .build())
                .addAllTokens(tokenFmcList)
                .build();
        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
        log.info("Successfully sent message: {}", response.getSuccessCount());
        if (response.getFailureCount() > 0) {
            List<SendResponse> responses = response.getResponses();
            List<String> failedTokens = new ArrayList<>();
            for (int i = 0; i < responses.size(); i++) {
                if (!responses.get(i).isSuccessful()) {
                    // The order of responses corresponds to the order of the registration tokens.
                    failedTokens.add(tokenFmcList.get(i));
                }
            }

            log.info("List of tokens that caused failures: " + failedTokens);
        }
    }

    public void sendNotificationNewAccount() throws ExecutionException, InterruptedException, FirebaseMessagingException {
        List<String> tokenFmcList = userService.getAdminsWithTokenFmc();

        if (tokenFmcList.isEmpty()) {
            log.info("No hay administradores registrados");
            return;
        }
        MulticastMessage message = MulticastMessage.builder()
                .setNotification(Notification.builder()
                        .setTitle("Nuevo usuario")
                        .setBody("Un usuario se ha unido a nuestra comunidad. Revisa su perfil")
                        .build())
                .addAllTokens(tokenFmcList)
                .build();
        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
        log.info("Successfully sent message: {}", response.getSuccessCount());
        if (response.getFailureCount() > 0) {
            List<SendResponse> responses = response.getResponses();
            List<String> failedTokens = new ArrayList<>();
            for (int i = 0; i < responses.size(); i++) {
                if (!responses.get(i).isSuccessful()) {
                    // The order of responses corresponds to the order of the registration tokens.
                    failedTokens.add(tokenFmcList.get(i));
                }
            }

            log.info("List of tokens that caused failures: " + failedTokens);
        }
    }


}