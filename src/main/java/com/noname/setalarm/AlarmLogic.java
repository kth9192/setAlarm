package com.noname.setalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.noname.setalarm.receiver.AlarmReceiver;
import com.noname.setalarm.repository.AlarmDao;

import java.util.Calendar;

public class AlarmLogic {

    private static String TAG = AlarmLogic.class.getSimpleName();
    private Context context;
    private Calendar calendar = Calendar.getInstance();
    private AlarmManager am;

    public AlarmLogic(Context context) {
        this.context = context;
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        calendar.setTimeInMillis(System.currentTimeMillis());
    }

    public void setToCalendar(int hour, int minute, boolean am_pm){
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.AM_PM, am_pm ? Calendar.AM : Calendar.PM);
    }

    public int getCurrentHour(){
        Calendar tmpCal = Calendar.getInstance();
        return tmpCal.get(Calendar.HOUR_OF_DAY);}

    public int getCurrentMinute(){
        Calendar tmpCal = Calendar.getInstance();
        return tmpCal.get(Calendar.MINUTE);}

    public int getCurrentSecond(){
        Calendar tmpCal = Calendar.getInstance();
        return tmpCal.get(Calendar.SECOND);}

    public long getCalendarTime(){
        return calendar.getTimeInMillis();
    }

    public int makeID(int hour, int minute, int second){
        Calendar tmpCal = Calendar.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append(hour);
        sb.append(minute);
        sb.append(second);
        sb.append(tmpCal.get(Calendar.MILLISECOND));

        return Integer.parseInt(sb.toString());
    }

    //pendingintent id는 무조건 고유해야함.
    public void newAlarm(int id, long time){

        Log.d(TAG , "알람등록");

        Intent tmpIntent = new Intent(context, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, tmpIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        if(System.currentTimeMillis() < time) {
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, time,
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }else {
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, time + AlarmManager.INTERVAL_DAY,
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }

    }
    public void unregisterAlarm(int id){
        Log.d(TAG , "알람취소");

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent unRegisterIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, id, unRegisterIntent, 0);
        am.cancel(sender);
    }
}
