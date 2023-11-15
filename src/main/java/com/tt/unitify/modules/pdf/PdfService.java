package com.tt.unitify.modules.pdf;

import com.google.cloud.Timestamp;
import com.tt.unitify.modules.bill.BillEntity;
import com.tt.unitify.modules.bill.BillService;
import com.tt.unitify.modules.building.BuildingEntity;
import com.tt.unitify.modules.building.BuildingService;
import com.tt.unitify.modules.departments.DepartmentEntity;
import com.tt.unitify.modules.departments.DepartmentService;
import com.tt.unitify.modules.dto.TimestampDto;
import com.tt.unitify.modules.paymentservices.PaymentServicesEntity;
import com.tt.unitify.modules.paymentservices.PaymentServicesService;
import com.tt.unitify.modules.payrollpayment.PayrollPaymentEntity;
import com.tt.unitify.modules.payrollpayment.PayrollPaymentService;
import com.tt.unitify.modules.pdf.dto.annualpaymentreport.AnnualPaymentReportDto;
import com.tt.unitify.modules.pdf.dto.annualpaymentreport.DepartmentDataDto;
import com.tt.unitify.modules.pdf.dto.annualpaymentreport.PaymentReportDto;
import com.tt.unitify.modules.pdf.dto.incomestatement.IncomeStatementDataDto;
import com.tt.unitify.modules.pdf.dto.incomestatement.IncomeStatementDto;
import com.tt.unitify.modules.pdf.dto.monthlydepartmentreport.MonthlyDepartmentReportDto;
import com.tt.unitify.modules.pdf.dto.monthlyreport.MiscellaneousExpenses;
import com.tt.unitify.modules.pdf.dto.monthlyreport.MonthlyReportDto;
import com.tt.unitify.modules.pdf.dto.payrollreport.PayrollData;
import com.tt.unitify.modules.pdf.dto.payrollreport.PayrollReportDto;
import com.tt.unitify.modules.reservefund.ReserveFundEntity;
import com.tt.unitify.modules.reservefund.ReserveFundService;
import com.tt.unitify.modules.users.UserService;
import com.tt.unitify.modules.utils.TransformUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.text.Format;
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
    @Autowired
    ReserveFundService reserveFundService;
    @Autowired
    PayrollPaymentService payrollPaymentService;
    @Autowired
    PaymentServicesService paymentServicesService;


    public byte[] generateMonthlyDepartmentReport(String idDepartment, String idBill) throws ExecutionException, InterruptedException {
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
        return pdfGenerator.monthlyDepartmentReport(monthlyDepartmentReportDto);
    }

    public PdfResponse generateIncomeReport(LocalDate localDate) throws ExecutionException, InterruptedException {

        IncomeStatementDto incomeReportDto = new IncomeStatementDto();
        Date date = convertToDateViaInstant(localDate);
        log.info("Generate Income Report - Date {}", date);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", new Locale("es", "ES"));
        SimpleDateFormat yearFormat = new SimpleDateFormat("YYYY", new Locale("es", "ES"));

        incomeReportDto.setMonth( monthFormat.format(date));
        incomeReportDto.setYear(yearFormat.format(date));

        TimestampDto timestampDto =TransformUtil.getTimestampDto(date);
        Timestamp startDate = timestampDto.getStartDateTime();
        Timestamp endDate = timestampDto.getEndDateTime();

        log.info("Find payments from: {} - to: {}", startDate, endDate);
        List<BillEntity> billPaymentsList=billService.paymentsByCompletedDateInMonth(startDate, endDate);
        log.info("BillPaymentsList: {}", billPaymentsList);
        List<IncomeStatementDataDto> incomeStatementDataDtoList = billService.incomeStatementDataDtoList(billPaymentsList);
        incomeReportDto.setData(incomeStatementDataDtoList);
        PdfResponse pdfResponse = pdfGenerator.incomeStatement(incomeReportDto);
        return pdfResponse;
    }

    public PdfResponse generateAnnualReport(LocalDate localDate, String idBuilding) throws ExecutionException, InterruptedException, FileNotFoundException {

        Date date = convertToDateViaInstant(localDate);
        log.info("Generate Annual Report - Date {}", date);
        //List<BillEntity> billEntities = billService.filterByYearAndBuilding(building, "2021");

        AnnualPaymentReportDto annualPaymentReportDto = new AnnualPaymentReportDto();
        annualPaymentReportDto.setYear(String.valueOf(localDate.getYear()));

        List<DepartmentDataDto> paymentDataList = new ArrayList<>();


        BuildingEntity buildingEntity = buildingService.findBuildingById(idBuilding);
        annualPaymentReportDto.setBuilding(buildingEntity.getName());
        if (buildingEntity!=null) {
            // Obtiene el ID del edificio
            String idBuildingId = buildingEntity.getId();
            // Consulta los departamentos relacionados con el edificio
            List<DepartmentEntity> departmentEntities= departmentService.findByBuilding(idBuildingId);
            int i = 0;
            // Recorre los departamentos y consulta las facturas relacionadas
            for (DepartmentEntity departmentEntity : departmentEntities) {
                log.info("Contador: {}", i);
                i++;
                String idDepartment = departmentEntity.getId();
                List<BillEntity> billEntities = billService.findByDepartmentAndIsPaid(idDepartment, date);
                log.info("BillEntities: {}", billEntities);
                DepartmentDataDto departmentDataDto = new DepartmentDataDto();
                departmentDataDto.setName(departmentEntity.getOwner());
                departmentDataDto.setDepartment(departmentEntity.getName());
                List<PaymentReportDto> paymentReportDtoList = new ArrayList<>();
                log.info("DepartmentDataDto: {}", departmentDataDto);
                for (BillEntity billEntity : billEntities) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(billEntity.getCorrespondingDate().toDate());

                    int month = calendar.get(Calendar.MONTH);
                    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", new Locale("es", "ES"));
                    Format dateFormat = new SimpleDateFormat("MM/dd/yy");

                    PaymentReportDto paymentReportDto = new PaymentReportDto(billEntity.getFolio(),dateFormat.format(billEntity.getCompletedDate().toDate()),monthFormat.format(billEntity.getCorrespondingDate().toDate()),month);
                    log.info("PaymentReportDto: {}", paymentReportDto);
                    paymentReportDtoList.add(paymentReportDto);
                }
                List<PaymentReportDto> paymentDtoList = new ArrayList<>();
                departmentDataDto.setPaymentDtoList(paymentReportDtoList);
                log.info("DepartmentDataDto: {}", departmentDataDto);
                paymentDataList.add(departmentDataDto);
            }
            annualPaymentReportDto.setPaymentDataList(paymentDataList);
            log.info("AnnualPaymentReportDto: {}", annualPaymentReportDto);
        } else {
            log.info("No se encontró el edificio con nombre: " + idBuilding);
        }

        PdfResponse pdfResponse =pdfGenerator.annualPaymentReport(annualPaymentReportDto);
        log.info("Generate Annual Report - Date {}", date);
        return pdfResponse;

    }

    public PdfResponse generateMonthlyReport(LocalDate localDate) throws ExecutionException, InterruptedException, FileNotFoundException {

        Date date = convertToDateViaInstant(localDate);
        TimestampDto timestampDto =TransformUtil.getTimestampDto(date);
        Timestamp startDate = timestampDto.getStartDateTime();
        Timestamp endDate = timestampDto.getEndDateTime();

        MonthlyReportDto monthlyDepartmentReportDto = new MonthlyReportDto();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", new Locale("es", "ES"));
        SimpleDateFormat yearFormat = new SimpleDateFormat("YYYY", new Locale("es", "ES"));
        monthlyDepartmentReportDto.setMonth(monthFormat.format(date));
        monthlyDepartmentReportDto.setYear(yearFormat.format(date));
        ReserveFundEntity reserveFundEntity = getReserveFund(date);
        monthlyDepartmentReportDto = getMonthlyReportDto(monthlyDepartmentReportDto, startDate,endDate, reserveFundEntity);
        monthlyDepartmentReportDto = getInfoForPayrollsPayments(date, monthlyDepartmentReportDto);
        MiscellaneousExpenses miscellaneousExpenses = getMiscellaneousExpenses(date);
        monthlyDepartmentReportDto.setOthersPayments(miscellaneousExpenses);

        //Todo subir fondo a base de datos
        PdfResponse pdfResponse=pdfGenerator.monthlyReport(monthlyDepartmentReportDto);
        double totalFund = pdfResponse.getAmount();
        log.info("TotalFund: {}", totalFund);
        log.info("Generate Monthly Report {}", monthlyDepartmentReportDto);
        reserveFundEntity.setAmount(String.valueOf(totalFund));
        log.info("Original Period: {} - {}", startDate, endDate);
        Timestamp starDateNextMonth = TransformUtil.addOneMonth(startDate);
        Timestamp endDateNextMonth = TransformUtil.addOneMonth(endDate);
        log.info("Next Period: {} - {}", starDateNextMonth, endDateNextMonth);
        Timestamp newDate = TransformUtil.addSevenDays(starDateNextMonth);
        reserveFundEntity.setDate(newDate);
        reserveFundService.createOrUpdate(starDateNextMonth,endDateNextMonth,reserveFundEntity);
        return pdfResponse;
    }

    public PdfResponse generatePayrollReport(LocalDate localDate, boolean isFirstFortnight) throws ExecutionException, InterruptedException, FileNotFoundException {
        Date date = convertToDateViaInstant(localDate);
        PayrollReportDto dto = new PayrollReportDto();
        dto.setFirstFortnight(isFirstFortnight);
        dto.setDate(date);
        List<PayrollPaymentEntity> payrollPaymentEntityList = new ArrayList<>();
        if (isFirstFortnight) {
            payrollPaymentEntityList = payrollPaymentService.getFirstPayrollPayment(date);
        } else {
            payrollPaymentEntityList = payrollPaymentService.getSecondPayrollPayment(date);
        }
        List<PayrollData> payrollDataList= new ArrayList<>();
        payrollDataList = TransformUtil.toListPayrollData(payrollPaymentEntityList);
        dto.setPayrollData(payrollDataList);
        PdfResponse pdfResponse =pdfGenerator.payrollReport(dto);
        log.info("Generate Payroll Report {}", dto);
        return pdfResponse;
    }


    private Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }



    private MonthlyReportDto getMonthlyReportDto(MonthlyReportDto dto, Timestamp startDate, Timestamp endDate, ReserveFundEntity reserveFundEntity) throws ExecutionException, InterruptedException {

        List<BillEntity> billPaymentsList= billService.findByMonthAndOrderByFolio(startDate, endDate);
        if (!billPaymentsList.isEmpty() && billPaymentsList!=null) {
            dto.setFolioStart(billPaymentsList.get(0).getFolio());
            dto.setFolioEnd(billPaymentsList.get(billPaymentsList.size()-1).getFolio());
        }else {
            dto.setFolioStart("x");
            dto.setFolioEnd("x");
        }


        double totalAmount = billService.totalAmount(billPaymentsList);
        dto.setTotalAmount(String.valueOf(totalAmount));
        if (reserveFundEntity!=null) {
            dto.setTotalFund(reserveFundEntity.getAmount());
        } else {
            dto.setTotalFund("0");
        }

        return dto;
    }

    private ReserveFundEntity getReserveFund(Date date) throws ExecutionException, InterruptedException {
        TimestampDto timestampDto =TransformUtil.getTimestampDto(date);
        Timestamp startDate = timestampDto.getStartDateTime();
        Timestamp endDate = timestampDto.getEndDateTime();
        ReserveFundEntity reserveFundEntity = reserveFundService.findByMonth(startDate, endDate);
        log.info("ReserveFundEntity: {}", reserveFundEntity);
        return reserveFundEntity;
    }

    private MonthlyReportDto getInfoForPayrollsPayments(Date date, MonthlyReportDto monthlyReportDto) throws ExecutionException, InterruptedException {
        List<PayrollPaymentEntity> firstPayrollPayment = payrollPaymentService.getFirstPayrollPayment(date);
        double firstPayrollAmount = payrollPaymentService.payrollAmount(firstPayrollPayment);
        monthlyReportDto.setFirstPayroll(String.valueOf(firstPayrollAmount));

        List<PayrollPaymentEntity> secondPayrollPayment = payrollPaymentService.getSecondPayrollPayment(date);
        double secondPayrollAmount = payrollPaymentService.payrollAmount(secondPayrollPayment);
        monthlyReportDto.setSecondPayroll(String.valueOf(secondPayrollAmount));

        return monthlyReportDto;
    }

    private MiscellaneousExpenses getMiscellaneousExpenses(Date date) throws ExecutionException, InterruptedException {
        MiscellaneousExpenses miscellaneousExpenses = new MiscellaneousExpenses();
        TimestampDto timestampDto =TransformUtil.getTimestampDto(date);
        Timestamp startDate = timestampDto.getStartDateTime();
        Timestamp endDate = timestampDto.getEndDateTime();

        log.info("Find payments from: {} - to: {}", startDate, endDate);

        List<PaymentServicesEntity> paymentServicesEntities = paymentServicesService.findBetweenDates(startDate, endDate);
        log.info("paymentServicesEntities: {}", paymentServicesEntities);

        miscellaneousExpenses.setTotalAmount(String.valueOf(paymentServicesService.totalAmount(paymentServicesEntities)));
        miscellaneousExpenses.setDescription(paymentServicesService.concatDescription(paymentServicesEntities));
        return miscellaneousExpenses;
    }
}
