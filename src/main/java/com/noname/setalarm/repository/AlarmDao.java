package com.noname.setalarm.repository;

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

    @Delete
    void deleteAlarm(AlarmRoom alarmRoom);

//    @Query("SELECT * from AlarmRoom where id = :id")
//    LiveData<AlarmRoom> getAlarm(String id);

    @Query("SELECT * from AlarmRoom")
    LiveData<List<AlarmRoom>> getAllAlarm();
}
