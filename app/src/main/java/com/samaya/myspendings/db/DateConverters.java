package com.samaya.myspendings.db;

import android.util.Log;

import androidx.room.TypeConverter;

import com.samaya.myspendings.Utils;

import java.util.Date;

public class DateConverters {
    @TypeConverter
    public static Date stringToDate(String value) {
        Date date = null;
        try{
            date = Utils.sqldtf.parse(value);
        }catch (Exception exception){
            Log.e("String to date", exception.getMessage());
        }
        return date;
    }

    @TypeConverter
    public static String dateToString(Date date) {
        return Utils.sqldtf.format(date);
    }
}