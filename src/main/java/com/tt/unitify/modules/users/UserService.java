package com.tt.unitify.modules.users;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Log4j2
@Service
public class UserService {
    public static final String COLLECTION="UNITIFY";
    public static final String USER_DOCUMENT="USER";
    public String createUser(UserDto user) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        //ApiFuture<WriteResult> collection = dbFirestore.collection(COLLECTION).document(USER_DOCUMENT).set(user);
        ApiFuture<DocumentReference> collection = dbFirestore.collection(COLLECTION).add(user);
        return collection.get().getId();
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
