package com.noname.setalarm.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

public class AlarmModel {

    private String id;

    private List<ClockModel> timeList;

    public AlarmModel( String id, List<ClockModel> timeList) {
        this.id = id;
        this.timeList = timeList;
    }

    public String getId() {
        return id;
    }

    public List<ClockModel> getTimeList() {
        return timeList;
    }
}
