package com.tt.unitify.modules.dto;

import com.google.cloud.Timestamp;
import lombok.Data;

@Data
public class TimestampDto {
    Timestamp startDateTime;
    Timestamp endDateTime;
}
