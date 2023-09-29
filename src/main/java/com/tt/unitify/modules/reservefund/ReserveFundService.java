package com.tt.unitify.modules.reservefund;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
@Log4j2
public class ReserveFundService {

    public static final String COLLECTION="RESERVE_FUND";
    private final Firestore db = FirestoreClient.getFirestore();


    public ReserveFundEntity findByMonth(Timestamp startDate, Timestamp endDate) throws ExecutionException, InterruptedException {
        CollectionReference ref = db.collection(COLLECTION);
        log.info("Find Reserve fund period: {} - {}", startDate, endDate);
        Query query = ref.whereGreaterThanOrEqualTo("date",startDate).whereLessThanOrEqualTo("date",endDate);
        List<ReserveFundEntity> billList = new ArrayList<>();
        for (DocumentSnapshot document : query.get().get().getDocuments()) {
            ReserveFundEntity entity = document.toObject(ReserveFundEntity.class);
            entity.setId(document.getId());
            billList.add(entity);
        }
        log.info("billList: {}", billList);
        ReserveFundEntity reserveFundEntity = new ReserveFundEntity();

        if (!billList.isEmpty()) {
            reserveFundEntity = billList.get(0);
            return reserveFundEntity;
        }
        return null;
    }
    public void createOrUpdate(Timestamp startDate, Timestamp endDate, ReserveFundEntity dto) throws ExecutionException, InterruptedException {
        log.info("Reserve-Fund createOrUpdate: {}", dto.getDate());
        log.info("CreateOrUpdate period: {} - {}", startDate, endDate);
        ReserveFundEntity reserveFundEntity = this.findByMonth(startDate,endDate);
        log.info("Find createOrUpdate: {}", reserveFundEntity);
        if (!Objects.isNull(reserveFundEntity)) {
            this.updateAmount(dto.getId(),dto.getAmount());
            log.info("Reserve-Fund update: {}", dto.getId());
        }else {
            ReserveFundDto reserveFundDto = new ReserveFundDto();
            reserveFundDto.setAmount(dto.getAmount());
            reserveFundDto.setDate(dto.getDate());
            String id=this.create(reserveFundDto);
            log.info("Reserve-Fund create: {}", id);
        }
    }


    public String create(ReserveFundDto entity) throws ExecutionException, InterruptedException {

        ApiFuture<DocumentReference> create = db.collection(COLLECTION).add(entity);
        log.info("Reserve-Fund create: {}", create.get().get());
        return create.get().getId();
    }

    /*public String update(String id,ReserveFundDto user) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION).document(id);
        ApiFuture<WriteResult> collection = db.collection(COLLECTION).document(USER_DOCUMENT).set(user);
        ApiFuture<WriteResult> future = docRef.update(UserDto, user);
        WriteResult result = future.get();
        log.info("Write result: {}",  result);
        return result.getUpdateTime().toString();
    }*/

    public void updateAmount(String id, String amount) throws ExecutionException, InterruptedException {

        DocumentReference docRef = db.collection(COLLECTION).document(id);
        ApiFuture<WriteResult> future = docRef.update("amount", amount);
        WriteResult result = future.get();
        log.info("Write result: {}",  result);
    }

}
