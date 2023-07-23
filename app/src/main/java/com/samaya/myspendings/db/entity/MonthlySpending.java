package com.samaya.myspendings.db.entity;

import androidx.room.ColumnInfo;

import java.util.Date;

public class MonthlySpending {

    @ColumnInfo(name = "amount")
    public Float amount;
    @ColumnInfo(name = "month")
    public String month;
}
