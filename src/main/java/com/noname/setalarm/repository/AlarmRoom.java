package com.noname.setalarm.repository;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.Relation;
import androidx.room.TypeConverters;
import androidx.annotation.NonNull;

import com.noname.setalarm.model.ClockModel;

import java.util.List;

@Entity
public class AlarmRoom {

    @PrimaryKey
    @NonNull
    private String alarmId;

    @TypeConverters(ClockTypeConverter.class)
    public final  List<ClockModel> timeList;

    private boolean checked;

    public AlarmRoom(@NonNull String alarmId, List<ClockModel> timeList, boolean checked) {
        this.alarmId = alarmId;
        this.timeList = timeList;
        this.checked = checked;
    }

    @NonNull
    public String getAlarmId() {
        return alarmId;
    }

    public List<ClockModel> getTimeList() {
        return timeList;
    }

    public boolean isChecked() {
        return checked;
    }
}
