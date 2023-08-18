package com.tt.unitify;

import com.tt.unitify.modules.pdf.PdfService;
import com.tt.unitify.modules.pdf.dto.incomestatement.IncomeStatementDataDto;
import com.tt.unitify.modules.pdf.dto.incomestatement.IncomeStatementDto;
import com.tt.unitify.modules.pdf.dto.monthlyreport.MiscellaneousExpenses;
import com.tt.unitify.modules.pdf.dto.monthlyreport.MonthlyReportDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@SpringBootApplication
public class UnitifyApplication {

	@Autowired
	PdfService pdfService;
	public static void main(String[] args) {
		SpringApplication.run(UnitifyApplication.class, args);
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
		data1.setMonthPaid("Enero 2022");
		data1.setAmount("1000");
		data1.setDate("Enero 2022");
		for (int i = 0; i <100 ; i++) {
			incomeStatementDtoList.add(data1);
		}
		data.setData(incomeStatementDtoList);
		pdfService.incomeStatement(data);
		log.info("PDF created");
	}

	@EventListener(ApplicationReadyEvent.class)
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
		pdfService.monthlyReport(data);

		log.info("PDF created");
	}

}
