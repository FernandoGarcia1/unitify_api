package com.tt.unitify.modules.utils;

import com.google.cloud.Timestamp;
import com.google.firebase.database.utilities.Pair;
import com.tt.unitify.modules.bill.BillEntity;
import com.tt.unitify.modules.dto.TimestampDto;
import com.tt.unitify.modules.pdf.dto.annualpaymentreport.PaymentReportDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.*;

public class TransformUtil {
    public static final String UTILITY_CLASS = "Utility class";

    private TransformUtil() {
        throw new IllegalStateException(UTILITY_CLASS);
    }

    public static ResponseEntity file(Optional<Pair<String, byte[]>> body) {
        return body.map(TransformUtil::okFile).orElse(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
    public static ResponseEntity okFile(Pair<String, byte[]> body) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + body.getFirst())
                .body(body.getSecond());
    }

    public static List<PaymentReportDto> billEntityToPaymentReportDto(List<BillEntity> billEntityList) {
        return null;
    }


    private static int getMonthFromDate(Timestamp date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.toDate().getTime());
        return calendar.get(Calendar.MONTH) + 1; // Los meses en Calendar son 0-indexed
    }


    public static TimestampDto getTimestampDto(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.setTime(date);

        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        Calendar calendarFirstDay = Calendar.getInstance();
        calendarFirstDay.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendarFirstDay.set(year, month, 1, 0, 0, 0);

        Calendar calendarLastDay = Calendar.getInstance();
        calendarLastDay.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendarLastDay.set(year, month, 31, 23, 59, 59);

        Date firstDay = calendarFirstDay.getTime();
        Date lastDay = calendarLastDay.getTime();


        Timestamp startDate = Timestamp.of(firstDay);
        Timestamp endDate = Timestamp.of(lastDay);

        TimestampDto timestampDto = new TimestampDto();
        timestampDto.setStartDateTime(startDate);
        timestampDto.setEndDateTime(endDate);
        return timestampDto;
    }

    public static TimestampDto getFirstPayrollPayment(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        Calendar calendarFirstDay = Calendar.getInstance();
        calendarFirstDay.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendarFirstDay.set(year, month, 1, 0, 0, 0);

        Calendar calendarLastDay = Calendar.getInstance();
        calendarLastDay.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendarLastDay.set(year, month, 15, 23, 59, 59);

        Date firstDay = calendarFirstDay.getTime();
        Date lastDay = calendarLastDay.getTime();
        Timestamp startDate = Timestamp.of(firstDay);
        Timestamp endDate = Timestamp.of(lastDay);

        TimestampDto timestampDto = new TimestampDto();
        timestampDto.setStartDateTime(startDate);
        timestampDto.setEndDateTime(endDate);
        return timestampDto;
    }

    public static TimestampDto getSecondPayrollPayment(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.setTime(date);

        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);


        Calendar calendarFirstDay = Calendar.getInstance();
        calendarFirstDay.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendarFirstDay.set(year, month, 16, 0, 0, 0);

        Calendar calendarLastDay = Calendar.getInstance();
        calendarLastDay.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendarLastDay.set(year, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);



        Date firstDay= calendarFirstDay.getTime();
        Date lastDay = calendarLastDay.getTime();

        Timestamp startDate = Timestamp.of(firstDay);
        Timestamp endDate = Timestamp.of(lastDay);
        TimestampDto timestampDto = new TimestampDto();
        timestampDto.setStartDateTime(startDate);
        timestampDto.setEndDateTime(endDate);
        return timestampDto;
    }

    public static Timestamp addOneMonth(Timestamp date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.toDate().getTime());
        calendar.add(Calendar.MONTH, 1);
        return Timestamp.of(calendar.getTime());
    }

    public static Timestamp addSevenDays(Timestamp date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.toDate().getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 7);
        return Timestamp.of(calendar.getTime());
    }












}
