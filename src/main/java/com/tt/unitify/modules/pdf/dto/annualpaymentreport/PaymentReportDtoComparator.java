package com.tt.unitify.modules.pdf.dto.annualpaymentreport;

import java.util.Comparator;

public class PaymentReportDtoComparator implements Comparator<PaymentReportDto> {
    @Override
    public int compare(PaymentReportDto o1, PaymentReportDto o2) {
        return Integer.compare(o1.getMonthNumber(), o2.getMonthNumber());
    }
}