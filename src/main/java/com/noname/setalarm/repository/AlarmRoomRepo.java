package com.noname.setalarm.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.noname.setalarm.model.ClockModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlarmRoomRepo {

    private AlarmDao alarmDao;
    private ClockDao clockDao;

    private LiveData<List<AlarmRoom>> alarmLiveData;
    private LiveData<List<ClockModel>> clockLiveData;
    private ExecutorService executorService;

    public AlarmRoomRepo(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);

        alarmDao = db.alarmDao();
        clockDao = db.clockDao();

        alarmLiveData = alarmDao.getAllAlarm();
        clockLiveData = clockDao.getAllClock();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<AlarmRoom>> getAllAlarm(){
        return alarmLiveData;
    }

    public LiveData<List<ClockModel>> getAllClock(){
        return clockLiveData;
    }

    public void insert(AlarmRoom alarmRoom) {
        executorService.execute(() -> alarmDao.insertAlarm(alarmRoom));
    }

    public void update(String id, List<ClockModel> timeList) {
        executorService.execute(() -> alarmDao.updateAlarm(id, timeList));
    }

    public void delete(AlarmRoom alarmRoom){
        executorService.execute(() -> alarmDao.deleteAlarm(alarmRoom));
    }

    public void updateAlarmState(String id, boolean checked){
        executorService.execute(() -> alarmDao.updateAlarmState(id, checked));
    }

    public void insertClockModel(ClockModel clockModel) {
        executorService.execute(() -> clockDao.insertClock(clockModel));
    }

    public void deleteClockModel(ClockModel clockModel){
        executorService.execute(() -> clockDao.deleteClock(clockModel));
    }

    public void deleteAllClock(){
        executorService.execute(() -> clockDao.deleteAll());
    }

    public void updateClockHour(int id, int hour){
        executorService.execute(() -> clockDao.updateClockHour(id, hour));
    }
    public void updateClockMinute(int id, int minute){
        executorService.execute(() -> clockDao.updateClockMinute(id, minute));
    }
    public void updateClockAMPM(int id, boolean am_pm){
        executorService.execute(() -> clockDao.updateClockAMPM(id, am_pm));
    }

}

