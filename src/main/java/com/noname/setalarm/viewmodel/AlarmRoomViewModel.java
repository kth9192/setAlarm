package com.noname.setalarm.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.noname.setalarm.repository.AlarmRoom;
import com.noname.setalarm.repository.AlarmRoomRepo;

import java.util.List;

public class AlarmRoomViewModel extends AndroidViewModel {

    private AlarmRoomRepo alarmRoomRepo;
    private LiveData<List<AlarmRoom>> listLiveData;

    public AlarmRoomViewModel(@NonNull Application application) {
        super(application);
        this.alarmRoomRepo = new AlarmRoomRepo(application);
    }

    public LiveData<List<AlarmRoom>> getListLiveData() {
        if (listLiveData == null){
            listLiveData = alarmRoomRepo.getAllAlarm();
        }
        return listLiveData;
    }

    public void insert(AlarmRoom alarmRoom) { alarmRoomRepo.insert(alarmRoom); }

    public void delete(AlarmRoom alarmRoom){alarmRoomRepo.delete(alarmRoom);}

    public void updateState(String id, boolean checked){alarmRoomRepo.updateAlarmState(id, checked);}

}
