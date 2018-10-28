package com.noname.setalarm.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity()
public class ClockModel implements Parcelable {

    @PrimaryKey
    @NonNull
    private int id;

    private int hour;
    private int minute;
    private boolean am_pm;

    public ClockModel(){}

    public ClockModel(@NonNull int id, int hour, int minute, boolean am_pm) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
//        am_pm = hour >= 12;
        this.am_pm = am_pm;
    }

    public ClockModel(Parcel in){
        this.id = in.readInt();
        this.hour =in.readInt();
        this.minute = in.readInt();
        this.am_pm = in.readByte() != 0;
    }

    public static final Creator<ClockModel> CREATOR = new Creator<ClockModel>() {
        @Override
        public ClockModel createFromParcel(Parcel in) {
            return new ClockModel(in);
        }

        @Override
        public ClockModel[] newArray(int size) {
            return new ClockModel[size];
        }
    };

    @NonNull
    public int getId(){
        return id;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean isAm_pm() {
        return am_pm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeByte((byte) (am_pm ? 1:0));
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setAm_pm(boolean am_pm) {
        this.am_pm = am_pm;
    }
}
