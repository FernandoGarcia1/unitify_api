package com.tt.unitify.modules.pdf.dto.annualpaymentreport;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class PaymentReportDto {
    String folio;
    String datePayment;
    String month;
    int monthNumber;
    boolean enabled; //Se usara para indicar si hay un pago registrado en ese mes, si es true folio y datePayment deben ser diferentes de null

    public PaymentReportDto(String folio, String datePayment, String month, int monthNumber) {
        this.folio = folio;
        this.datePayment = datePayment;
        this.month = month;
        this.enabled = true;
        this.monthNumber = monthNumber;
    }

    public PaymentReportDto(String month, boolean enabled) {
        this.enabled = enabled;
        this.month = month;
    }
}
