package com.tt.unitify.modules.parkingcontrol;

import com.google.cloud.Timestamp;
import lombok.Data;



@Data
public class ParkingControlDto {
    Timestamp dueDate;
    String idDepartment;
}
