package com.tt.unitify.modules.users;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Log4j2
@Service
public class UserService {
    public static final String COLLECTION="USERS";
    private final Firestore db = FirestoreClient.getFirestore();
    public String createUser(UserEntity user) throws ExecutionException, InterruptedException {

        //ApiFuture<WriteResult> collection = dbFirestore.collection(COLLECTION).document(USER_DOCUMENT).set(user);
        ApiFuture<DocumentReference> collection = db.collection(COLLECTION).add(user);
        return collection.get().getId();
    }

    public UserEntity findUser(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(COLLECTION).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        UserEntity entity = document.toObject(UserEntity.class);
        if ( entity != null) {
            log.info("Document: {}", document);
            log.info("Future: {}", future.get().getId());
            entity.setId(future.get().getId());
            return entity;
        } else {
            log.info("No such document!");
            return null;
        }
    }

    /*public String update(String id,UserDto user) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION).document(id);
        ApiFuture<WriteResult> collection = db.collection(COLLECTION).document(USER_DOCUMENT).set(user);
        ApiFuture<WriteResult> future = docRef.update(UserDto, user);
        WriteResult result = future.get();
        log.info("Write result: {}",  result);
        return result.getUpdateTime().toString();
    }*/
}
