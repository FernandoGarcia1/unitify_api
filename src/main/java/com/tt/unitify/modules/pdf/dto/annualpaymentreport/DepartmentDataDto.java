package com.tt.unitify.modules.pdf.dto.annualpaymentreport;

import lombok.Data;

import java.util.List;

@Data
public class DepartmentDataDto {
    String department;
    String name;
    List<PaymentReportDto> paymentDtoList; //El tamaño de la lista debera ser 12 (una por cada mes), si no se tiene el dato se debera enviar un objeto con el atributo enabled en false
}
