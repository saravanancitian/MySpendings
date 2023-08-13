package com.samaya.myspendings.db;

import android.util.Log;

import androidx.room.TypeConverter;

import com.samaya.myspendings.utils.DateUtils;

import java.util.Date;

public class DateConverters {
    @TypeConverter
    public static Date stringToDate(String value) {
        Date date = null;
        try{
            date = DateUtils.sqldtf.parse(value);
        }catch (Exception exception){
            Log.e("String to date", exception.getMessage());
        }
        return date;
    }

    @TypeConverter
    public static String dateToString(Date date) {
        return DateUtils.sqldtf.format(date);
    }
}