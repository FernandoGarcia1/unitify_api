package com.tt.unitify.modules.bill;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BillEntity extends BillDto{
    String id;
}
