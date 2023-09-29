package com.tt.unitify.modules.pdf;

import com.tt.unitify.modules.bill.BillDto;
import com.tt.unitify.modules.bill.BillService;
import com.tt.unitify.modules.departments.DepartmentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/generate-monthly-department")
    public void generateMonthlyPayrollReport(@RequestParam(required = true) String idDepartment, @RequestParam(required = true) String idBill) throws ExecutionException, InterruptedException, ExecutionException {
        pdfService.generateMonthlyDepartmentReport(idDepartment, idBill);
    }

    @GetMapping("/generate-income-report")
    public void generateIncomeReport(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date)throws InterruptedException, ExecutionException {
        pdfService.generateIncomeReport(date);
    }

    @GetMapping("/annual-report")
    public void annualReport(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date, @RequestParam("building") String building) throws InterruptedException, ExecutionException, FileNotFoundException {
        pdfService.generateAnnualReport(date, building);
    }

    @GetMapping("/monthly-report")
    public void monthlyReport(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date) throws InterruptedException, ExecutionException, FileNotFoundException {
        pdfService.generateMonthlyReport(date);
    }


    @PostMapping
    public void create(@RequestBody BillDto dto) throws ExecutionException, InterruptedException, ExecutionException {
        log.info("BillDto: {}", dto);
        billService.create(dto);
    }
}
