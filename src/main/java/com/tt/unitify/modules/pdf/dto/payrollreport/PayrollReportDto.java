package com.tt.unitify.modules.pdf.dto.payrollreport;

import java.util.Date;
import java.util.List;



import lombok.Data;

@Data
public class PayrollReportDto {
    private boolean firstFortnight;
    private List<PayrollData> payrollData;
    private Date date;
}
