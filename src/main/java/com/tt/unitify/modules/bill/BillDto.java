package com.tt.unitify.modules.bill;

import com.google.cloud.Timestamp;
import lombok.Data;



@Data
public class BillDto {
    String idDepartment;
    Timestamp creationDate;
    String description;
    boolean isPaid;
    String totalAmount;
    String amountPaid;
    String idBillType;
    String folio;
    Timestamp  correspondingDate;
    Timestamp completedDate;
}
