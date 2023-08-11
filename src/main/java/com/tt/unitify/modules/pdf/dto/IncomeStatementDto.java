package com.tt.unitify.modules.pdf.dto;

import lombok.Data;

import java.util.List;

@Data
public class IncomeStatementDto {
    private String month;
    private String year;
    private List<IncomeStatementDataDto> data;
}
