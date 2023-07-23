package com.tt.unitify.modules.buildingbalance;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BuildingBalanceEntity extends BuildingBalanceDto{
    String id;
}
