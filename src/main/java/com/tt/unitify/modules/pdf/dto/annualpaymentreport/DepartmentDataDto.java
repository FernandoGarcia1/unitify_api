package com.tt.unitify.modules.pdf.dto.annualpaymentreport;

import lombok.Data;

import java.util.List;

@Data
public class DepartmentDataDto implements Comparable<DepartmentDataDto>{
    String department;
    String name;
    List<PaymentReportDto> paymentDtoList; //El tamaño de la lista debera ser 12 (una por cada mes), si no se tiene el dato se debera enviar un objeto con el atributo enabled en false

    @Override
    public int compareTo(DepartmentDataDto other) {
        // Comparar numéricamente por el campo 'folio'
        Integer thisDepartmen = Integer.parseInt(this.getDepartment());
        Integer otherDepartment = Integer.parseInt(other.getDepartment());

        return thisDepartmen.compareTo(otherDepartment);
    }
}
