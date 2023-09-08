package com.tt.unitify.modules.pdf;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Date;

@Log4j2
@Service
public class PdfService {


    public void generateMonthlyPayrollReport(Date date, String idUser) {

        log.info("PdfService.generateMonthlyPayrollReport");
    }
}
