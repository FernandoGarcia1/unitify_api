package com.tt.unitify.modules.pdf.dto;

import lombok.Data;

@Data
public class IncomeStatementDataDto {
    private String invoice;
    private String building;
    private String department;
    private String monthPaid;
    private String amount;
    private String date;
}
