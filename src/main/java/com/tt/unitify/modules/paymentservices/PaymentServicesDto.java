package com.tt.unitify.modules.paymentservices;

import com.google.cloud.Timestamp;
import lombok.Data;

@Data
public class PaymentServicesDto {
    String name;
    String description;
    String amount;
    String urlVoucher;
    Timestamp  creationDate;
    String idAdmin;
}
