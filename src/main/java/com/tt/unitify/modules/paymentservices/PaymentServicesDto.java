package com.tt.unitify.modules.paymentservices;

import lombok.Data;

@Data
public class PaymentServicesDto {
    String name;
    String description;
    String amount;
    String urlVoucher;
    String creationDate;
    String idAdmin;
}
