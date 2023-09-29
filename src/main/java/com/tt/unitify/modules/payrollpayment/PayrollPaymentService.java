package com.tt.unitify.modules.payrollpayment;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.firebase.cloud.FirestoreClient;
import com.tt.unitify.modules.dto.TimestampDto;
import com.tt.unitify.modules.utils.TransformUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Log4j2
@Service
public class PayrollPaymentService  {

    public static final String COLLECTION="PAYROLL_PAYMENT";
    private final Firestore db = FirestoreClient.getFirestore();

    public PayrollPaymentEntity findById(String id) {
        return null;
    }

    public List<PayrollPaymentEntity> getFirstPayrollPayment(Date date) throws ExecutionException, InterruptedException {
        TimestampDto timestampDto = TransformUtil.getFirstPayrollPayment(date);
        log.info("timestampDto: {}", timestampDto);
        return findBetweenDates(timestampDto.getStartDateTime(),timestampDto.getEndDateTime());

    }

    public List<PayrollPaymentEntity> getSecondPayrollPayment(Date date) throws ExecutionException, InterruptedException {
        TimestampDto timestampDto = TransformUtil.getSecondPayrollPayment(date);
        log.info("timestampDto: {}", timestampDto);
        return findBetweenDates(timestampDto.getStartDateTime(),timestampDto.getEndDateTime());
    }


    public List<PayrollPaymentEntity> findBetweenDates(Timestamp startDate, Timestamp endDate) throws ExecutionException, InterruptedException {

        CollectionReference ref = db.collection(COLLECTION);
        Query query = ref.whereGreaterThanOrEqualTo("date",startDate).whereLessThanOrEqualTo("date",endDate);
        List<PayrollPaymentEntity> paymentEntityList = new ArrayList<>();
        for (DocumentSnapshot document : query.get().get().getDocuments()) {
            log.info("document: {}", document.getData());
            PayrollPaymentEntity entity = document.toObject(PayrollPaymentEntity.class);
            entity.setId(document.getId());
            paymentEntityList.add(entity);
        }
        log.info("paymentEntityList: {}", paymentEntityList);
        return paymentEntityList;
    }

    public double payrollAmount(List<PayrollPaymentEntity> payrollPaymentEntityList) {
        double totalAmount = 0;
        for (PayrollPaymentEntity entity : payrollPaymentEntityList) {
            totalAmount += Double.parseDouble(entity.getAmount());
        }
        return totalAmount;
    }
}
