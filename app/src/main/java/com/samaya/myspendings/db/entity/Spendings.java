package com.samaya.myspendings.db.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.samaya.myspendings.utils.Utils;

import java.util.Date;

@Entity(tableName = "spendings")
public class Spendings  implements Parcelable {
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

    public Spendings(){
        state = 0;
    }
    public Spendings(Spendings spending){
        if(spending != null) {
            this.ID = spending.ID;
            this.paidto = spending.paidto;
            this.amount = spending.amount;
            this.whendt = spending.whendt;
            this.remark = spending.remark;
            this.state = spending.state;
            this.createdDt = spending.createdDt;
            this.updatedDt = spending.updatedDt;
        }
    }
    protected Spendings(Parcel in){
        ID = in.readInt();
        paidto = in.readString();
        if (in.readByte() == 0) {
            amount = null;
        } else {
            amount = in.readFloat();
        }
        remark = in.readString();
        if (in.readByte() == 0) {
            state = null;
        } else {
            state = in.readInt();
        }
        try {
            whendt = Utils.sdtf.parse(in.readString());
            createdDt = Utils.sqldtf.parse(in.readString());
            updatedDt = Utils.sqldtf.parse(in.readString());
        }catch (Exception exception) {
            Log.e("Spendings", exception.getLocalizedMessage());
        }
    }

    public static final Creator<Spendings> CREATOR = new Creator<Spendings>() {
        @Override
        public Spendings createFromParcel(Parcel in) {
            return new Spendings(in);
        }

        @Override
        public Spendings[] newArray(int size) {
            return new Spendings[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(ID);
        parcel.writeString(paidto);
        if (amount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(amount);
        }
        parcel.writeString(remark);
        if (state == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(state);
        }
        parcel.writeString(Utils.sdtf.format(whendt));
        parcel.writeString(Utils.sqldtf.format(createdDt));
        parcel.writeString(Utils.sqldtf.format(updatedDt));
    }
}
