package com.tt.unitify.modules.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EventEntity extends EventDto{
    String id;
}
