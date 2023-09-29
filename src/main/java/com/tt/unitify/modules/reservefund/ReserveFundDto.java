package com.tt.unitify.modules.reservefund;

import com.google.cloud.Timestamp;
import lombok.Data;

@Data
public class ReserveFundDto {
    String amount;
    Timestamp date;
}
