package com.tt.unitify.modules.pdf.dto.incomestatement;

import lombok.Data;

import java.util.Date;

@Data
public class IncomeStatementDataDto implements Comparable<IncomeStatementDataDto>{
    private String invoice;
    private String building;
    private String department;
    private Date monthPaid;
    private String amount;
    private Date date;

    @Override
    public int compareTo(IncomeStatementDataDto other) {
        // Comparar numéricamente por el campo 'folio'
        Integer thisFolio = Integer.parseInt(this.getInvoice());
        Integer otherFolio = Integer.parseInt(other.getInvoice());

        return thisFolio.compareTo(otherFolio);
    }
}
