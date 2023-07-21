package com.tt.unitify.modules.departments;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DepartmentEntity extends DepartmentDto {
    String id;
}

