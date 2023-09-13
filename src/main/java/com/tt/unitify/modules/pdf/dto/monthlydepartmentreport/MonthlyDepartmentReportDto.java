package com.tt.unitify.modules.pdf.dto.monthlydepartmentreport;

import com.google.cloud.Timestamp;
import lombok.Data;

import java.util.Date;
@Data
public class MonthlyDepartmentReportDto {
    String owner;
    Date correspondingDate;
    String amount;
    String folio;
    String department;
    String building;
    String description;
    Date completedDate;
}
