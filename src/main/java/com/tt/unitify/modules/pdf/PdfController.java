package com.tt.unitify.modules.pdf;

import com.tt.unitify.modules.bill.BillDto;
import com.tt.unitify.modules.bill.BillService;
import com.tt.unitify.modules.departments.DepartmentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.concurrent.ExecutionException;



@Log4j2
@RestController
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    PdfService pdfService;

    @Autowired
    BillService billService;

    @Autowired
    DepartmentService departmentService;

    @GetMapping("/payment-report")
    public byte [] generateMonthlyPayrollReport(@RequestParam(required = true) String idDepartment, @RequestParam(required = true) String idBill, @Context HttpServletResponse response) throws ExecutionException, InterruptedException, ExecutionException {
        log.info("payment-report idDepartment: {}, idBill: {}", idDepartment, idBill);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "comprobante_de_pago_".concat(idBill).concat(".pdf"));
        return pdfService.generateMonthlyDepartmentReport(idDepartment, idBill);
    }

    @GetMapping("/generate-income-report")
    public byte[] generateIncomeReport(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date, @Context HttpServletResponse response)throws InterruptedException, ExecutionException {
        log.info("generate-income-report date: {}", date);
        PdfResponse pdfResponse =pdfService.generateIncomeReport(date);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, pdfResponse.getName());
        return pdfResponse.getData();
    }

    @GetMapping("/annual-report")
    public byte[] annualReport(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date, @RequestParam("building") String building, @Context HttpServletResponse response) throws InterruptedException, ExecutionException, FileNotFoundException {
        log.info("annual-report date: {}, building: {}", date, building);
        PdfResponse pdfResponse =pdfService.generateAnnualReport(date, building);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, pdfResponse.getName());
        return pdfResponse.getData();
    }

    @GetMapping("/monthly-report")
    public byte[] monthlyReport(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date, @Context HttpServletResponse response) throws InterruptedException, ExecutionException, FileNotFoundException {
        log.info("monthly-report date: {}", date);
        PdfResponse pdfResponse =pdfService.generateMonthlyReport(date);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, pdfResponse.getName());
        return pdfResponse.getData();
    }

    @GetMapping("/payroll-report")
    public byte[] payrollReport(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date, @RequestParam("isFirstFortnight") String isFirstFortnight, @Context HttpServletResponse response) throws InterruptedException, ExecutionException, FileNotFoundException {
        log.info("payroll-report date: {}, isFirstFortnight: {}", date, isFirstFortnight);
        boolean firstFortnight = Boolean.parseBoolean(isFirstFortnight);
        PdfResponse pdfResponse =pdfService.generatePayrollReport(date, firstFortnight);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, pdfResponse.getName());
        return pdfResponse.getData();
    }
    
}
