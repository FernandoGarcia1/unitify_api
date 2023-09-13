package com.tt.unitify.modules.departments;

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
public class DepartmentService {
    public static final String COLLECTION="DEPARTMENT";
    private final Firestore db = FirestoreClient.getFirestore();

    public DepartmentEntity findDepartmentByID(String id) throws ExecutionException, InterruptedException {
        log.info("findDepartmentByID: {}", id);
        DocumentReference docRef = db.collection(COLLECTION).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        DepartmentEntity entity = document.toObject(DepartmentEntity.class);
        if ( entity != null) {
            entity.setId(future.get().getId());
            return entity;
        } else {
        log.info("Department not found!");
            return null;
        }
    }
}
