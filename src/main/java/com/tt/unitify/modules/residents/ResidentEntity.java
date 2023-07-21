package com.tt.unitify.modules.residents;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResidentEntity extends ResidentDto{
    String id;
}
