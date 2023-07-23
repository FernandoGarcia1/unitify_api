package com.tt.unitify.modules.event;

import lombok.Data;

@Data
public class EventDto {
    String name;
    String description;
    String date;
    String idEventType;
    String idAdmin;
}
