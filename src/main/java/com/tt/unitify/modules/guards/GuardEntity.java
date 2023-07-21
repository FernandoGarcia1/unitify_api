package com.tt.unitify.modules.guards;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GuardEntity extends GuardDto{
    String id;
}
