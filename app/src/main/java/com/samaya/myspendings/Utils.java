package com.samaya.myspendings;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;

public class Utils {

    public static SimpleDateFormat sqlpdtf = new SimpleDateFormat("MM-yyyy");
    public static SimpleDateFormat sqldtf
            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat sdf
            = new SimpleDateFormat("dd-MMM-yyyy");
    public static SimpleDateFormat stf
            = new SimpleDateFormat("HH:mm");

    public static SimpleDateFormat sdtf
            = new SimpleDateFormat("dd-MMM-yyyy HH:mm");

    public static DateFormatSymbols dfs = new DateFormatSymbols();
    public static String getMonth(int idx){
       final String months[] = dfs.getMonths();
        return  months[idx];
    }

    public static String getShortMonths(int idx){
        final String months[] = dfs.getShortMonths();
        return  months[idx];
    }

}
