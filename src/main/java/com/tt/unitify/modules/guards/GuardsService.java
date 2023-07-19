package com.tt.unitify.modules.guards;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.tt.unitify.modules.users.UserEntity;
import com.tt.unitify.modules.users.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@Log4j2
public class GuardsService {
    public static final String COLLECTION="GUARDS";
    private final Firestore dbFirestore = FirestoreClient.getFirestore();
    @Autowired
    UserService userService;
    public String createGuard(GuardEntity guard) throws ExecutionException, InterruptedException {

        UserEntity user = userService.findUser(guard.getIdUser());
        log.info("User: {}", user);
        ApiFuture<DocumentReference> create = dbFirestore.collection(COLLECTION).add(guard);
        log.info("Guard: {}", create.get().get());
        return create.get().getId();
    }
}
