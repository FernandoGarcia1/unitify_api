package com.tt.unitify.modules.pdf.dto.annualpaymentreport;

import lombok.Data;

import java.util.List;

@Data
public class AnnualPaymentReportDto {
    String year;
    String building;
    List<DepartmentDataDto> paymentDataList;
}
