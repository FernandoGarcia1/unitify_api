package com.tt.unitify.modules.pdf.dto.monthlyreport;

import lombok.Data;

@Data
public class MonthlyReportDto {
    String month;
    String year;
    String folioStart;
    String folioEnd;
    String totalAmount;
    String totalFund; //Fondo de reserva
    String firstPayroll;
    String secondPayroll;
    MiscellaneousExpenses othersPayments;
}
