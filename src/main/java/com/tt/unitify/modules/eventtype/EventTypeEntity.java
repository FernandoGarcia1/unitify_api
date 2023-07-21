package com.tt.unitify.modules.eventtype;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EventTypeEntity extends EventTypeDto{
    String id;
}
