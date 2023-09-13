package com.tt.unitify.modules.pdf;

import com.google.cloud.Timestamp;
import com.tt.unitify.modules.bill.BillEntity;
import com.tt.unitify.modules.bill.BillService;
import com.tt.unitify.modules.building.BuildingEntity;
import com.tt.unitify.modules.building.BuildingService;
import com.tt.unitify.modules.departments.DepartmentEntity;
import com.tt.unitify.modules.departments.DepartmentService;
import com.tt.unitify.modules.pdf.dto.incomestatement.IncomeStatementDataDto;
import com.tt.unitify.modules.pdf.dto.incomestatement.IncomeStatementDto;
import com.tt.unitify.modules.pdf.dto.monthlydepartmentreport.MonthlyDepartmentReportDto;
import com.tt.unitify.modules.users.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Log4j2
@Service
public class PdfService {

    @Autowired
    UserService userService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    BillService billService;
    @Autowired
    BuildingService buildingService;
    @Autowired
    PdfGenerator pdfGenerator;


    public void generateMonthlyDepartmentReport(String idDepartment, String idBill) throws ExecutionException, InterruptedException {
        MonthlyDepartmentReportDto monthlyDepartmentReportDto = new MonthlyDepartmentReportDto();
        DepartmentEntity department = departmentService.findDepartmentByID(idDepartment);
        monthlyDepartmentReportDto.setOwner(department.getOwner());
        monthlyDepartmentReportDto.setDepartment(department.getName());

        BillEntity bill = billService.findBillById(idBill);
        monthlyDepartmentReportDto.setAmount(bill.getTotalAmount());
        monthlyDepartmentReportDto.setDescription(bill.getDescription());

        BuildingEntity building = buildingService.findBuildingById(department.getIdBuilding());
        monthlyDepartmentReportDto.setBuilding(building.getName());
        monthlyDepartmentReportDto.setFolio(bill.getFolio());
        log.info("GenerateMonthlyPayrollReport-1: {}", monthlyDepartmentReportDto);

        log.info("Date-2: {}", bill.getCreationDate().toDate());
        monthlyDepartmentReportDto.setCorrespondingDate(bill.getCorrespondingDate().toDate());
        monthlyDepartmentReportDto.setCompletedDate(bill.getCompletedDate().toDate());
        pdfGenerator.monthlyDepartmentReport(monthlyDepartmentReportDto);

    }

    public void generateIncomeReport(LocalDate localDate) throws ExecutionException, InterruptedException {

        IncomeStatementDto incomeReportDto = new IncomeStatementDto();
        Date date = convertToDateViaInstant(localDate);
        log.info("Generate Income Report - Date {}", date);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", new Locale("es", "ES"));


        SimpleDateFormat yearFormat = new SimpleDateFormat("YYYY", new Locale("es", "ES"));

        incomeReportDto.setMonth( monthFormat.format(date));
        incomeReportDto.setYear(yearFormat.format(date));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        Date firstDay = new GregorianCalendar(year, month, 1).getTime();
        Date lastDay = new GregorianCalendar(year, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)).getTime();


        Timestamp startDate = Timestamp.of(firstDay);
        Timestamp endDate = Timestamp.of(lastDay);

        log.info("Find payments from: {} - to: {}", startDate, endDate);
        List<BillEntity> billPaymentsList=billService.findByMonth(startDate, endDate);
        log.info("BillPaymentsList: {}", billPaymentsList);
        List<IncomeStatementDataDto> incomeStatementDataDtoList = billService.incomeStatementDataDtoList(billPaymentsList);
        incomeReportDto.setData(incomeStatementDataDtoList);
        pdfGenerator.incomeStatement(incomeReportDto);
    }


    private Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}
