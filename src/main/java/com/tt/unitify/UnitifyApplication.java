package com.tt.unitify;

import com.tt.unitify.modules.pdf.PdfGenerator;
import com.tt.unitify.modules.pdf.dto.incomestatement.IncomeStatementDataDto;
import com.tt.unitify.modules.pdf.dto.incomestatement.IncomeStatementDto;
import com.tt.unitify.modules.pdf.dto.monthlyreport.MiscellaneousExpenses;
import com.tt.unitify.modules.pdf.dto.monthlyreport.MonthlyReportDto;
import com.tt.unitify.modules.pdf.dto.payrollreport.PayrollData;
import com.tt.unitify.modules.pdf.dto.payrollreport.PayrollReportDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Log4j2
@SpringBootApplication
public class UnitifyApplication {

	@Autowired
	PdfGenerator pdfGenerator;


	public static void main(String[] args) {
		SpringApplication.run(UnitifyApplication.class, args);
	}

	//@EventListener(ApplicationReadyEvent.class)
	public void pdfMonthlyDepartmentReportExample() throws FileNotFoundException {
		pdfGenerator.monthlyDepartmentReport(null);
		log.info("PDF created");
	}
	//@EventListener(ApplicationReadyEvent.class)
	public void pdfIncomeStatementExample(){
		IncomeStatementDto data = new IncomeStatementDto();
		data.setMonth("Enero");
		data.setYear("2023");
		List<IncomeStatementDataDto> incomeStatementDtoList = new ArrayList<>();
		IncomeStatementDataDto data1 = new IncomeStatementDataDto();
		data1.setInvoice("123456");
		data1.setBuilding("B");
		data1.setDepartment("301");
		//data1.setMonthPaid("Enero 2022");
		data1.setAmount("1000");
		//data1.setDate("Enero 2022");
		for (int i = 0; i <100 ; i++) {
			incomeStatementDtoList.add(data1);
		}
		data.setData(incomeStatementDtoList);
		pdfGenerator.incomeStatement(data);
		log.info("PDF created");
	}

	//@EventListener(ApplicationReadyEvent.class)
	public void pdfMonthlyReportExample() throws FileNotFoundException {
		MonthlyReportDto data = new MonthlyReportDto();
		data.setMonth("Enero");
		data.setYear("2023");
		data.setFolioStart("1000");
		data.setFolioEnd("1900");
		data.setTotalAmount("54009.54");
		data.setTotalFund("41000.22");
		data.setFirstPayroll("10000");
		data.setSecondPayroll("12000.65");
		MiscellaneousExpenses miscellaneousExpenses = new MiscellaneousExpenses();
		miscellaneousExpenses.setDescription("Limpieza, agua, luz.");
		miscellaneousExpenses.setTotalAmount("1000");
		data.setOthersPayments(miscellaneousExpenses);
		pdfGenerator.monthlyReport(data);

		log.info("PDF created");
	}
	//@EventListener(ApplicationReadyEvent.class)
	/*private void pdfAnnualReportExample() throws FileNotFoundException {
		AnnualPaymentReportDto data = new AnnualPaymentReportDto();
		data.setYear("2023");
		data.setBuilding("B");

		List<DepartmentDataDto> paymentDataList = new ArrayList<>();
		DepartmentDataDto departmentDataDto1 = new DepartmentDataDto();
		departmentDataDto1.setDepartment("301");
		departmentDataDto1.setName("Juan Perez Rodriguez");
		List<PaymentReportDto> paymentReportDtoList1 = new ArrayList<>();
		paymentReportDtoList1.add(new PaymentReportDto("Enero", "12/01/2022", "1"));
		paymentReportDtoList1.add(new PaymentReportDto("Febrero", "12/02/2022", "2"));
		paymentReportDtoList1.add(new PaymentReportDto("Marzo", "12/03/2022", "3"));
		paymentReportDtoList1.add(new PaymentReportDto("Abril", "12/04/2022", "4"));
		paymentReportDtoList1.add(new PaymentReportDto("Mayo", "12/05/2022", "5"));
		paymentReportDtoList1.add(new PaymentReportDto("Junio", "12/06/2022", "6"));
		paymentReportDtoList1.add(new PaymentReportDto("Julio", "12/07/2022", "7"));
		paymentReportDtoList1.add(new PaymentReportDto("Agosto", "12/08/2022", "8"));
		paymentReportDtoList1.add(new PaymentReportDto("Septiembre", "12/09/2022", "9"));
		paymentReportDtoList1.add(new PaymentReportDto("Octubre", "12/10/2022", "10"));
		paymentReportDtoList1.add(new PaymentReportDto("Noviembre", "12/11/2022", "11"));
		paymentReportDtoList1.add(new PaymentReportDto("Diciembre", "12/12/2022", "12"));
		departmentDataDto1.setPaymentDtoList(paymentReportDtoList1);
		paymentDataList.add(departmentDataDto1);



		DepartmentDataDto departmentDataDto2 = new DepartmentDataDto();
		departmentDataDto2.setDepartment("302");
		departmentDataDto2.setName("Maria Angeles Perez Rodriguez");
		List<PaymentReportDto> paymentReportDtoList2 = new ArrayList<>();
		paymentReportDtoList2.add(new PaymentReportDto("Enero", "12/01/2022", "1"));
		paymentReportDtoList2.add(new PaymentReportDto("Febrero", "12/02/2022", "2"));
		paymentReportDtoList2.add(new PaymentReportDto("3",false));
		paymentReportDtoList2.add(new PaymentReportDto("4",false));
		paymentReportDtoList2.add(new PaymentReportDto("Mayo", "12/02/2022", "5"));
		paymentReportDtoList2.add(new PaymentReportDto("Junio", "12/02/2022", "6"));
		paymentReportDtoList2.add(new PaymentReportDto("7",false));
		paymentReportDtoList2.add(new PaymentReportDto("Agosto", "12/02/2022", "8"));
		paymentReportDtoList2.add(new PaymentReportDto("9",false));
		paymentReportDtoList2.add(new PaymentReportDto("Octubre", "12/02/2022", "10"));
		paymentReportDtoList2.add(new PaymentReportDto("11",false));
		paymentReportDtoList2.add(new PaymentReportDto("Diciembre", "12/02/2022", "12"));
		departmentDataDto2.setPaymentDtoList(paymentReportDtoList2);

		DepartmentDataDto departmentDataDto3 = new DepartmentDataDto();
		departmentDataDto3.setDepartment("312");
		departmentDataDto3.setName("Maria Angeles Perez Rodriguez Rodriguez Rodriguez Rodriguez");
		List<PaymentReportDto> paymentReportDtoList3 = new ArrayList<>();
		paymentReportDtoList3.add(new PaymentReportDto("1",false));
		paymentReportDtoList3.add(new PaymentReportDto("2",false));
		paymentReportDtoList3.add(new PaymentReportDto("3",false));
		paymentReportDtoList3.add(new PaymentReportDto("4",false));
		paymentReportDtoList3.add(new PaymentReportDto("5",false));
		paymentReportDtoList3.add(new PaymentReportDto("456465AFG", "12/02/2022", "6"));
		paymentReportDtoList3.add(new PaymentReportDto("7",false));
		paymentReportDtoList3.add(new PaymentReportDto("8",false));
		paymentReportDtoList3.add(new PaymentReportDto("9",false));
		paymentReportDtoList3.add(new PaymentReportDto("12234AHGF", "12/02/2022", "10"));
		paymentReportDtoList3.add(new PaymentReportDto("11",false));
		paymentReportDtoList3.add(new PaymentReportDto("765GF17J","12/02/2022", "12"));
		departmentDataDto3.setPaymentDtoList(paymentReportDtoList3);

		paymentDataList.add(departmentDataDto2);
		paymentDataList.add(departmentDataDto1);
		paymentDataList.add(departmentDataDto2);
		paymentDataList.add(departmentDataDto1);
		paymentDataList.add(departmentDataDto2);
		paymentDataList.add(departmentDataDto2);
		paymentDataList.add(departmentDataDto1);
		paymentDataList.add(departmentDataDto1);
		paymentDataList.add(departmentDataDto3);
		paymentDataList.add(departmentDataDto2);
		paymentDataList.add(departmentDataDto1);

		data.setPaymentDataList(paymentDataList);


		pdfGenerator.annualPaymentReport(data);
		log.info("PDF created");
	}*/

	@EventListener(ApplicationReadyEvent.class)
	private void pdfPayrollExample() throws FileNotFoundException {
		PayrollReportDto data = new PayrollReportDto();
		data.setFirstFortnight(true);
		data.setDate(new Date());
		List<PayrollData> payrollDataList = new ArrayList<>();
		PayrollData payrollData1 = new PayrollData();
		payrollData1.setAmount(1240.60);
		PayrollData payrollData2 = new PayrollData();
		payrollData2.setAmount(3440.60);
		payrollDataList.add(payrollData1);
		payrollDataList.add(payrollData2);
		data.setPayrollData(payrollDataList);
		pdfGenerator.payrollReport(data);
		log.info("PDF created");
	}

	@EventListener(ApplicationReadyEvent.class)
	private void testCollectionPayment() throws FileNotFoundException, ExecutionException, InterruptedException {
		//paymentService.getPayments();
		log.info("testCollectionPayment Ready");
	}
}
