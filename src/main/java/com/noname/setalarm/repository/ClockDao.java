package com.noname.setalarm.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.noname.setalarm.model.ClockModel;

import java.util.List;

@Dao
public interface ClockDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertClock(ClockModel... clockModels);

    @Delete
    void deleteClock(ClockModel clockModel);

//    @Query("SELECT * from AlarmRoom where id = :id")
//    LiveData<AlarmRoom> getAlarm(String id);

    @Query("SELECT * from ClockModel")
    LiveData<List<ClockModel>> getAllClock();

    @Query("UPDATE ClockModel SET hour= :hour WHERE id = :id")
    void updateClockHour(int id , int hour);

    @Query("UPDATE ClockModel SET minute= :minute WHERE id = :id")
    void updateClockMinute(int id, int minute);

    @Query("UPDATE ClockModel SET am_pm = :am_pm WHERE id = :id")
    void updateClockAMPM(int id, boolean am_pm);

    @Query("DELETE FROM ClockModel")
    void deleteAll();
}
