package com.tt.unitify.modules.parkingcontrol;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.tt.unitify.modules.bill.BillEntity;
import com.tt.unitify.modules.bill.BillService;
import com.tt.unitify.modules.departments.DepartmentEntity;
import com.tt.unitify.modules.departments.DepartmentService;
import com.tt.unitify.modules.dto.TimestampDto;
import com.tt.unitify.modules.qr.QrService;
import com.tt.unitify.modules.utils.ResponseDto;
import com.tt.unitify.modules.utils.TransformUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Log4j2
@Service
public class ParkingControllerService {

    public static final String COLLECTION="PARKING_CONTROL";
    private final Firestore db = FirestoreClient.getFirestore();

    @Autowired
    BillService billService;

    @Autowired
    QrService qrService;

    @Autowired
    DepartmentService departmentService;


    public String create(ParkingControlDto parkingControlDto) throws ExecutionException, InterruptedException {
        ApiFuture<DocumentReference> collection = db.collection(COLLECTION).add(parkingControlDto);
        return collection.get().getId();
    }

    ParkingControlEntity findByMonthAndIdDepartment(Timestamp startDate, Timestamp endDate, String idDepartment) throws ExecutionException, InterruptedException {
        CollectionReference ref = db.collection(COLLECTION);
        log.info("startDate: {}", startDate);
        log.info("endDate: {}", endDate);
        log.info("idDepartment: {}", idDepartment);
        Query query = ref.whereGreaterThanOrEqualTo("dueDate",startDate).whereLessThanOrEqualTo("dueDate",endDate).whereEqualTo("idDepartment", idDepartment);
        List<ParkingControlEntity> parkingControlEntityList = new ArrayList<>();
        for (DocumentSnapshot document : query.get().get().getDocuments()) {
            ParkingControlEntity entity = document.toObject(ParkingControlEntity.class);
            entity.setId(document.getId());
            parkingControlEntityList.add(entity);
        }
        if (parkingControlEntityList.isEmpty()) {
            return null;
        }
        return parkingControlEntityList.get(0);
    }




    public ResponseEntity<ResponseDto> validateAccess(String idDepartment) throws ExecutionException, InterruptedException {

        DepartmentEntity department= departmentService.findDepartmentByID(idDepartment);
        log.info("LLega aqui: {}", department);
        if (department == null) {
            log.info("El idDepartment no es válido.");
            String message = "2";
            log.info("message: {}", message);
            ResponseDto responseDto = new ResponseDto(message);
            log.info("responseDto: {}", responseDto);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        log.info("department: {}", department);

        TimestampDto timestampDto = getTimestampDto();
        Timestamp startDate = timestampDto.getStartDateTime();
        Timestamp endDate = timestampDto.getEndDateTime();

        ParkingControlEntity parkingControlEntity;
        parkingControlEntity = findByMonthAndIdDepartment(startDate, endDate, idDepartment);
        log.info("ParkingControlEntity: {}", parkingControlEntity);


        if (parkingControlEntity == null) {
            log.info("No existe el registro en la tabla de control de acceso. Se debe de validar si el departamento es candidato a tener acceso.");
            boolean hasAccess = hasAccess(idDepartment);
            log.info("hasAccess: {}", hasAccess);
            if (hasAccess) {
                log.info("El departamento es candidato a tener acceso. Se debe de guardar el registro en la tabla de control de acceso.");
                ParkingControlDto parkingControlDto = new ParkingControlDto();
                parkingControlDto.setIdDepartment(idDepartment);
                parkingControlDto.setDueDate(timestampDto.getEndDateTime());
                log.info("parkingControlDto: {}", parkingControlDto);
                String idParkingControl = create(parkingControlDto);
                log.info("idParkingControl: {}", idParkingControl);

                return new ResponseEntity<>(new ResponseDto("0"), HttpStatus.OK);
            }else {
                log.info("El departamento no es candidato a tener acceso.");
                return new ResponseEntity<>(new ResponseDto("1"), HttpStatus.OK);
            }

        }else {
            log.info("Existe el registro en la tabla de control de acceso. El departamento tiene acceso.");
            return new ResponseEntity<>(new ResponseDto("0"), HttpStatus.OK);
        }
    }

    private TimestampDto getTimestampDto() {
        LocalDate localDate = LocalDate.now();
        Date date = convertToDateViaInstant(localDate);
        return TransformUtil.getTimestampDto(date);
    }

    private Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }


    public byte [] generateQrForDepartment(String idDepartment) throws IOException, WriterException {
        // Encoding charset
        Map<EncodeHintType, ErrorCorrectionLevel> hashMap
                = new HashMap<EncodeHintType,
                ErrorCorrectionLevel>();
        hashMap.put(EncodeHintType.ERROR_CORRECTION,
                ErrorCorrectionLevel.L);
        return qrService.createQR(idDepartment, 500, 500);

    }

    public boolean hasAccess (String idDepartment) throws ExecutionException, InterruptedException {
        log.info("Metodo hasAccess...");
        LocalDate actualDate = LocalDate.now();

        //Evaluar el mes anterior
        LocalDate firstMonthLocalDate = actualDate.minusMonths(1);
        Date firstMonthDate = convertToDateViaInstant(firstMonthLocalDate);
        log.info("firstMonthDate: {}", firstMonthDate);
        TimestampDto firstMonthTimestampDto = TransformUtil.getTimestampDto(firstMonthDate);
        BillEntity firstMonthBillEntity = billService.findByMonthAndIdDepartmentAndBillTypeAndIsPaid(idDepartment,firstMonthTimestampDto.getStartDateTime(), firstMonthTimestampDto.getEndDateTime());
        Boolean firstMonthPaid = firstMonthBillEntity != null;

        log.info("El mes anterior fue pagado?: {}", firstMonthPaid);

        //Evaluar dos meses antes
        LocalDate secondMonthLocalDate = actualDate.minusMonths(2);
        Date secondMonthDate = convertToDateViaInstant(secondMonthLocalDate);
        log.info("secondMonthDate: {}", secondMonthDate);
        TimestampDto secondMonthTimestampDto = TransformUtil.getTimestampDto(secondMonthDate);
        BillEntity secondMonthBillEntity = billService.findByMonthAndIdDepartmentAndBillTypeAndIsPaid(idDepartment,secondMonthTimestampDto.getStartDateTime(), secondMonthTimestampDto.getEndDateTime());
        Boolean secondMonthPaid = secondMonthBillEntity != null;

        log.info("El segundo mes anterior fue pagado?: {}", secondMonthPaid);

        //Evaluar tres meses antes
        LocalDate thirdMonthLocalDate = actualDate.minusMonths(3);
        Date thirdMonthDate = convertToDateViaInstant(thirdMonthLocalDate);
        log.info("thirdMonthDate: {}", thirdMonthDate);
        TimestampDto thirdMonthTimestampDto = TransformUtil.getTimestampDto(thirdMonthDate);
        BillEntity thirdMonthBillEntity = billService.findByMonthAndIdDepartmentAndBillTypeAndIsPaid(idDepartment,thirdMonthTimestampDto.getStartDateTime(), thirdMonthTimestampDto.getEndDateTime());
        Boolean thirdMonthPaid = thirdMonthBillEntity != null;

        log.info("El tercer mes anterior fue pagado?: {}", thirdMonthPaid);

        return firstMonthPaid && secondMonthPaid && thirdMonthPaid;

    }

}
