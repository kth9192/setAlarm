package com.noname.setalarm.view;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.noname.setalarm.model.ClockModel;
import com.noname.setalarm.repository.AlarmRoom;

import java.util.List;

public class AlarmObserver extends BaseObservable {

    private List<ClockModel> tmpList;
    private boolean checked;

    public AlarmObserver(AlarmRoom alarmRoom) {
        tmpList = alarmRoom.getTimeList();
        checked = alarmRoom.isChecked();
    }

    @Bindable
    public String getTimeTxt() {

        StringBuilder result = new StringBuilder();
        for (ClockModel model : tmpList){
            result.append(model.getHour() + ":" + model.getMinute());
            result.append(model.isAm_pm()? "PM":"AM");
            result.append(" ");
        }

        return result.toString();
    }

}
