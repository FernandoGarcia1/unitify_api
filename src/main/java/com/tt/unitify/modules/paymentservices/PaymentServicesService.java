package com.tt.unitify.modules.paymentservices;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Log4j2
@Service
public class PaymentServicesService {
    public static final String COLLECTION="PAYMENT_SERVICES";
    private final Firestore db = FirestoreClient.getFirestore();





    public List<PaymentServicesEntity> findBetweenDates(Timestamp startDate, Timestamp endDate) throws ExecutionException, InterruptedException {
        CollectionReference ref = db.collection(COLLECTION);
        Query query = ref.whereGreaterThanOrEqualTo("creationDate",startDate).whereLessThanOrEqualTo("creationDate",endDate);
        List<PaymentServicesEntity> paymentList = new ArrayList<>();
        for (DocumentSnapshot document : query.get().get().getDocuments()) {
            PaymentServicesEntity entity = document.toObject(PaymentServicesEntity.class);
            entity.setId(document.getId());
            paymentList.add(entity);
        }

        if(!paymentList.isEmpty()) {
            return paymentList;
        }
        return Collections.emptyList();
    }

    public double totalAmount (List<PaymentServicesEntity> paymentServicesEntityList) {
        double totalAmount = 0;
        for (PaymentServicesEntity paymentServicesEntity : paymentServicesEntityList) {
            totalAmount += Double.parseDouble(paymentServicesEntity.getAmount());
        }
        return totalAmount;
    }

    public String concatDescription (List<PaymentServicesEntity> paymentServicesEntityList) {
        String description = "";
        for (PaymentServicesEntity paymentServicesEntity : paymentServicesEntityList) {
            description += paymentServicesEntity.getDescription() + ". ";
        }
        return description;
    }

}
