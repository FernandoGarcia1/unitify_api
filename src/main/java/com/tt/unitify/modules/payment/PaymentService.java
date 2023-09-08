package com.tt.unitify.modules.payment;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.protobuf.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Log4j2
@Service
public class PaymentService {
    public static final String COLLECTION="PAYMENT";
    private final Firestore db = FirestoreClient.getFirestore();

    public void getPayments() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> query = db.collection(COLLECTION).get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();

        for (QueryDocumentSnapshot document : documents) {
            PaymentEntity entity = document.toObject(PaymentEntity.class);
            entity.setId(document.getId());
            log.info("Document: {}", entity);
            log.info("Id: {}", entity.getId());
        }

    }

    public void getPaymentsWithWhere() throws ExecutionException, InterruptedException {
        CollectionReference collection = db.collection(COLLECTION);
        Query query = collection.whereEqualTo("amount", "100");
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
            PaymentEntity entity = document.toObject(PaymentEntity.class);
            entity.setId(document.getId());
            log.info("Document: {}", entity);
            log.info("Id: {}", entity.getId());
        }
    }
}
