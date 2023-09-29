package com.tt.unitify.modules.utils;

import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static boolean isFirstPayrollPayment(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int numberDay = calendar.get(Calendar.DAY_OF_MONTH);
        return numberDay <= 15;
    }
}
