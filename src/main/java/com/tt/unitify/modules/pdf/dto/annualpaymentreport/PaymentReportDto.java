package com.tt.unitify.modules.pdf.dto.annualpaymentreport;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentReportDto {
    String folio;
    String datePayment;
    String month;
    boolean enabled; //Se usara para indicar si hay un pago registrado en ese mes, si es true folio y datePayment deben ser diferentes de null

    public PaymentReportDto(String folio, String datePayment, String month) {
        this.folio = folio;
        this.datePayment = datePayment;
        this.month = month;
        this.enabled = true;
    }

    public PaymentReportDto(String month, boolean enabled) {
        this.enabled = enabled;
        this.month = month;
    }
}
