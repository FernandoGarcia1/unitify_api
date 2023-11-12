package com.tt.unitify.modules.users;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Log4j2
@Service
public class UserService {
    public static final String COLLECTION="USERS";
    private final Firestore db = FirestoreClient.getFirestore();
    public String createUser(UserDto user) throws ExecutionException, InterruptedException {

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

    public List<String> getUsersWithTokenFmc() throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();

        // Obtén la referencia a la colección "users"
        Iterable<QueryDocumentSnapshot> documents = firestore.collection(COLLECTION).get().get();

        List<String> usersWithTokenFmc = new ArrayList<>();

        // Itera sobre los documentos y agrega aquellos que tienen el campo "tokenFmc"
        for (QueryDocumentSnapshot document : documents) {
            if (document.contains("tokenFmc") && (document.getString("tokenFmc") != null)) {
                    usersWithTokenFmc.add(document.getString("tokenFmc"));

            }
        }
        return usersWithTokenFmc;
    }

    public List<String>  getAdminsWithTokenFmc() throws ExecutionException, InterruptedException {

        CollectionReference reference = db.collection(COLLECTION);

        Query query = reference.whereEqualTo("rol", "ADMIN");
        List<String> usersWithTokenFmc = new ArrayList<>();

        for (DocumentSnapshot document : query.get().get().getDocuments()) {

            UserEntity entity = document.toObject(UserEntity.class);
            entity.setId(document.getId());
            if (entity.getTokenFmc() != null && !entity.getTokenFmc().isEmpty()) {
                usersWithTokenFmc.add(entity.getTokenFmc());
            }

        }

        return usersWithTokenFmc;
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
