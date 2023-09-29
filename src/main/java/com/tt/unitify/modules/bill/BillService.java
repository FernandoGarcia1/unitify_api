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
import java.util.Collections;
import java.util.Comparator;
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

    public List<BillEntity> findByDepartmentAndIsPaid(String idDepartment) throws ExecutionException, InterruptedException {

        CollectionReference bill = db.collection(COLLECTION);

        Query query = bill.whereEqualTo("idDepartment", idDepartment).whereEqualTo("paid", true);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<BillEntity> billList = new ArrayList<>();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            BillEntity entity = document.toObject(BillEntity.class);
            if ( entity != null) {
                entity.setId(document.getId());
                billList.add(entity);
            } else {
                log.info("Department not found!");
            }

        }
        return billList;
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
        Query query = ref.whereGreaterThanOrEqualTo("completedDate",startDate).whereLessThanOrEqualTo("completedDate",endDate).whereEqualTo("paid", true);
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

    public List<BillEntity> findByMonthAndOrderByFolio(Timestamp startDate, Timestamp endDate) throws ExecutionException, InterruptedException {
        List<BillEntity> billList = findByMonth(startDate, endDate);
        log.info("BillList: {}", billList);
        Collections.sort(billList, Comparator.comparing(BillEntity::getFolio));
        return billList;
    }




    public List<BillEntity> filterByYearAndBuilding(String idBuilding, String year) throws ExecutionException, InterruptedException {

        BuildingEntity buildingEntity = buildingService.findBuildingById(idBuilding);
        List<BillEntity> finalList = new ArrayList<>();
        if (buildingEntity!=null) {
            // Obtiene el ID del edificio
            String idBuildingId = buildingEntity.getId();
            // Consulta los departamentos relacionados con el edificio
            List<DepartmentEntity> departmentEntities= departmentService.findByBuilding(idBuildingId);

            // Recorre los departamentos y consulta las facturas relacionadas
            for (DepartmentEntity departmentEntity : departmentEntities) {
                String idDepartment = departmentEntity.getId();
                List<BillEntity> billEntities = findByDepartmentAndIsPaid(idDepartment);
                finalList.addAll(billEntities);
            }
        } else {
            log.info("No se encontró el edificio con nombre: " + idBuilding);
        }
        return finalList;
    }

    public double totalAmount(List<BillEntity> billEntities) {
        double totalAmount = 0;
        for (BillEntity billEntity : billEntities) {
            totalAmount += Double.parseDouble(billEntity.getTotalAmount());
        }
        return totalAmount;
    }

}
