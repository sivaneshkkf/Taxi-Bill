package com.example.taxibill.DB;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class Date_Formats {

    public static SimpleDateFormat dateFormatDb = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    public static SimpleDateFormat dateFormatFile = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault());
    public static SimpleDateFormat dateFormatDbDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static SimpleDateFormat dateFormatDbTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    public static SimpleDateFormat dateFormatNative = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
    public static SimpleDateFormat dateFormatNative2 = new SimpleDateFormat("dd-MMM-yyyy\nhh:mm a", Locale.getDefault());
    public static SimpleDateFormat dateFormatNativeDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    public static SimpleDateFormat dateFormatNativeDate3 = new SimpleDateFormat("MMM-dd", Locale.getDefault());
    public static SimpleDateFormat dateFormatNativeDate4 = new SimpleDateFormat("MMM-dd-yy", Locale.getDefault());
    public static SimpleDateFormat dateFormatNativeDate1 = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
    public static SimpleDateFormat dateFormatNativeDate2 = new SimpleDateFormat("dd/MMM/yy", Locale.getDefault());
    public static SimpleDateFormat dateFormatNativeDate5 = new SimpleDateFormat("dd/MMM", Locale.getDefault());
    public static SimpleDateFormat dateFormatNativeTime = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    public static SimpleDateFormat dateFormatTimeStampChat = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault());
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    public static SimpleDateFormat yearyyyy = new SimpleDateFormat("yyyy", Locale.getDefault());
    public static SimpleDateFormat monthMM = new SimpleDateFormat("MM", Locale.getDefault());
    public static SimpleDateFormat datedd = new SimpleDateFormat("dd", Locale.getDefault());
    public static SimpleDateFormat monthMMM = new SimpleDateFormat("MMM", Locale.getDefault());
    public static SimpleDateFormat dayEEEE = new SimpleDateFormat("EEEE", Locale.getDefault());




    public static String getCurrentDateFormat(SimpleDateFormat simpleDateFormat){
        Calendar c=Calendar.getInstance();
        String date=simpleDateFormat.format(c.getTime());
        return date;
    }

    public static String getDateFormat(SimpleDateFormat simpleDateFormat,int year,int month,int date){
        Calendar c=Calendar.getInstance();
        c.set(year,month,date);
        String dateformat=simpleDateFormat.format(c.getTime());
        return dateformat;
    }

}
