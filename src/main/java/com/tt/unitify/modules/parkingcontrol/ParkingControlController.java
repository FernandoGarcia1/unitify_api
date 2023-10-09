package com.tt.unitify.modules.parkingcontrol;

import com.google.zxing.WriterException;
import com.tt.unitify.modules.qr.QrService;
import com.tt.unitify.modules.utils.ResponseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Log4j2
@RestController
@RequestMapping("/parking-control")
public class ParkingControlController {

    @Autowired
    ParkingControllerService parkingControllerService;
    @Autowired
    QrService qrService;

    @PostMapping("/{id}")
    public byte[] generateQr(@PathVariable String id) throws IOException, WriterException {
        log.info("id: {}", id);
        return parkingControllerService.generateQrForDepartment(id);
    }

    @GetMapping("/validate-access/{id}")
    public ResponseEntity<ResponseDto> validateAccess(@PathVariable String id) throws ExecutionException, InterruptedException {
        log.info("idDepartment: {}", id);
        return parkingControllerService.validateAccess(id);
    }

    @GetMapping()
    public String validateAccess(){
        log.info("idDepartment: test");
        return "test";
    }
}