package com.tt.unitify.modules.payrollpayment;

import com.google.cloud.Timestamp;
import lombok.Data;



@Data
public class PayrollPaymentDto {
    String name;
    String amount;
    Timestamp date;
    String workstation;
    String idAdmin;
    String description;
}
