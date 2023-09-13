package com.tt.unitify.modules.building;

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
public class BuildingService {
    public static final String COLLECTION="BUILDING";
    private final Firestore db = FirestoreClient.getFirestore();

    public BuildingEntity findBuildingById(String id) throws ExecutionException, InterruptedException {
        log.info("findBuildingById: {}", id);
        DocumentReference docRef = db.collection(COLLECTION).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        BuildingEntity entity = document.toObject(BuildingEntity.class);
        if ( entity != null) {
            entity.setId(future.get().getId());
            return entity;
        } else {
            log.info("Building not found!");
            return null;
        }
    }
}
