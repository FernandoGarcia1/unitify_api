package com.tt.unitify.modules.bill;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.tt.unitify.modules.building.BuildingEntity;
import com.tt.unitify.modules.building.BuildingService;
import com.tt.unitify.modules.departments.DepartmentEntity;
import com.tt.unitify.modules.departments.DepartmentService;
import com.tt.unitify.modules.pdf.dto.incomestatement.IncomeStatementDataDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Log4j2
@Service
public class BillService {
    public static final String COLLECTION="BILL";
    private final Firestore db = FirestoreClient.getFirestore();

    @Autowired
    DepartmentService departmentService;

    @Autowired
    BuildingService buildingService;

    public String create(BillDto bill) throws ExecutionException, InterruptedException {

        //ApiFuture<WriteResult> collection = dbFirestore.collection(COLLECTION).document(USER_DOCUMENT).set(user);
        ApiFuture<DocumentReference> collection = db.collection(COLLECTION).add(bill);
        return collection.get().getId();
    }


    public BillEntity findBillById(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = db.collection(COLLECTION).document(id);

        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        BillEntity entity = document.toObject(BillEntity.class);
        if ( entity != null) {
            entity.setId(future.get().getId());
            return entity;
        } else {
            log.info("Bill not found!");
            return null;
        }
    }


    public List<BillEntity> findByMonth(Timestamp startDate, Timestamp endDate) throws ExecutionException, InterruptedException {
        CollectionReference ref = db.collection(COLLECTION);
        Query query = ref.whereGreaterThanOrEqualTo("completedDate",startDate).whereLessThanOrEqualTo("completedDate",endDate);
        List<BillEntity> billList = new ArrayList<>();
        for (DocumentSnapshot document : query.get().get().getDocuments()) {
            BillEntity entity = document.toObject(BillEntity.class);
            entity.setId(document.getId());
            billList.add(entity);
        }
        return billList;
    }

    public List<IncomeStatementDataDto> incomeStatementDataDtoList (List<BillEntity> billEntities) throws ExecutionException, InterruptedException {
        List<IncomeStatementDataDto> incomeStatementDataDtoList = new ArrayList<>();
        for (BillEntity billEntity : billEntities) {

            DepartmentEntity departmentEntity = departmentService.findDepartmentByID(billEntity.getIdDepartment());
            BuildingEntity buildingEntity = buildingService.findBuildingById(departmentEntity.getIdBuilding());

            IncomeStatementDataDto incomeStatementDataDto = new IncomeStatementDataDto();
            incomeStatementDataDto.setInvoice(billEntity.getFolio());
            incomeStatementDataDto.setBuilding(buildingEntity.getName());
            incomeStatementDataDto.setDepartment(departmentEntity.getName());
            incomeStatementDataDto.setMonthPaid(billEntity.getCorrespondingDate().toDate());
            incomeStatementDataDto.setAmount(billEntity.getTotalAmount());
            incomeStatementDataDto.setDate(billEntity.getCompletedDate().toDate());
            incomeStatementDataDtoList.add(incomeStatementDataDto);
        }
        return incomeStatementDataDtoList;
    }

}
