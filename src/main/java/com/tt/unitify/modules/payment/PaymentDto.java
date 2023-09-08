package com.tt.unitify.modules.payment;

import com.google.cloud.Timestamp;
import lombok.Data;

@Data
public class PaymentDto {
    String amount;
    Timestamp date;
    String idBill;
}
