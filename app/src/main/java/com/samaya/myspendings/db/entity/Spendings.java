package com.samaya.myspendings.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "spendings")
public class Spendings {
    @Ignore
    public static final int STATE_CREATED = 1;
    @Ignore
    public static final int STATE_READY_TO_DELETED = 2;

    public static final int STATE_DELETED = 3;

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


    @ColumnInfo(name = "state")
    public Integer state;

    @ColumnInfo(name = "createdDt")
    public Date createdDt;

    @ColumnInfo(name = "updatedDt")
    public Date updatedDt;



}
