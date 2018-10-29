package com.noname.setalarm.repository;

import com.noname.setalarm.model.ClockModel;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAlarm(AlarmRoom... alarmRoom);

    @Query("UPDATE AlarmRoom SET timeList= :timeList WHERE alarmId = :id")
    void updateAlarm(String id, List<ClockModel> timeList);

    @Query("UPDATE AlarmRoom SET checked= :checked WHERE alarmId = :id")
    void updateAlarmState(String id , boolean checked);

    @Delete
    void deleteAlarm(AlarmRoom alarmRoom);

    @Query("SELECT * from AlarmRoom")
    LiveData<List<AlarmRoom>> getAllAlarm();
}
