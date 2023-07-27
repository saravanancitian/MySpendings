package com.samaya.myspendings.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "spendings")
public class Spendings {
    @PrimaryKey(autoGenerate = true)
    public int ID;

    @ColumnInfo(name = "paidto")
    public String paidto;

    @ColumnInfo(name = "amount")
    public Float amount;

    @ColumnInfo(name = "whendt")
    public Date whendt;

    @ColumnInfo(name = "remark")
    public String remark;

}
