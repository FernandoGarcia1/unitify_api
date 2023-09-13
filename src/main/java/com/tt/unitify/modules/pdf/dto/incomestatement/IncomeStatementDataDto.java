package com.tt.unitify.modules.pdf.dto.incomestatement;

import lombok.Data;

import java.util.Date;

@Data
public class IncomeStatementDataDto {
    private String invoice;
    private String building;
    private String department;
    private Date monthPaid;
    private String amount;
    private Date date;
}
