package com.noname.setalarm.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.noname.setalarm.model.ClockModel;
import com.noname.setalarm.repository.AlarmRoom;
import com.noname.setalarm.repository.AlarmRoomRepo;

import java.util.List;

public class ClockViewModel extends AndroidViewModel {

    private AlarmRoomRepo alarmRoomRepo;
    private LiveData<List<ClockModel>> listLiveData;

    public ClockViewModel(@NonNull Application application) {
        super(application);
        this.alarmRoomRepo = new AlarmRoomRepo(application);
    }

    public LiveData<List<ClockModel>> getListLiveData() {
        if (listLiveData == null){
            listLiveData = alarmRoomRepo.getAllClock();
        }
        return listLiveData;
    }


    public void insert(ClockModel clockModel) { alarmRoomRepo.insertClockModel(clockModel); }

    public void insertAlarm(AlarmRoom alarmRoom){alarmRoomRepo.insert(alarmRoom);}

    public void updateAlarm(String id, List<ClockModel> timeList){alarmRoomRepo.update(id, timeList);}

    public void delete(ClockModel clockModel){alarmRoomRepo.deleteClockModel(clockModel);}

    public void deleteAll(){alarmRoomRepo.deleteAllClock();}

    public void updateHour(int id, int hour){alarmRoomRepo.updateClockHour(id, hour);}

    public void updateMinute(int id, int minute){alarmRoomRepo.updateClockMinute(id, minute);}

    public void updateAMPM(int id, boolean am_pm){alarmRoomRepo.updateClockAMPM(id, am_pm);}

}
