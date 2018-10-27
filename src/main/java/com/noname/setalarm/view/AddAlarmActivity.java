package com.noname.setalarm.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TimePicker;

import com.noname.setalarm.AlarmLogic;
import com.noname.setalarm.R;
import com.noname.setalarm.databinding.ActivityAddalarmBinding;
import com.noname.setalarm.model.ClockModel;
import com.noname.setalarm.repository.AlarmRoom;
import com.noname.setalarm.viewmodel.ClockViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class AddAlarmActivity extends AppCompatActivity {

    private static String TAG = AddAlarmActivity.class.getSimpleName();
    private ActivityAddalarmBinding activityAddalarmBinding;

    private AlarmLogic alarmLogic;
    private static String uid = java.util.UUID.randomUUID().toString();
    private ClockViewModel viewModel;
    private SelectionTracker selectionTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityAddalarmBinding = DataBindingUtil.setContentView(this, R.layout.activity_addalarm);
        activityAddalarmBinding.setLifecycleOwner(this);
        activityAddalarmBinding.setProvider(new ImageProvider(R.drawable.hills, R.drawable.moon));
        setSupportActionBar(activityAddalarmBinding.toolbar);

        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 12){
            activityAddalarmBinding.pm.setVisibility(View.INVISIBLE);
        }else {
            activityAddalarmBinding.pm.setVisibility(View.VISIBLE);
        }

        alarmLogic = new AlarmLogic(this);
//        activityAddalarmBinding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(ClockViewModel.class);

        ArrayList<ClockModel> models = new ArrayList<>();
        alarmLogic.getCurrentHour();
        alarmLogic.getCurrentMinute();

        Log.d(TAG, "HOUR + " + alarmLogic.getCurrentHour() + "MINUTE : " + alarmLogic.getCurrentMinute());
        ClockModel clockModel = new ClockModel(alarmLogic.makeID(alarmLogic.getCurrentHour(), alarmLogic.getCurrentMinute(), alarmLogic.getCurrentSecond()),
                alarmLogic.getCurrentHour(), alarmLogic.getCurrentMinute(), alarmLogic.getCurrentHour()<12);
        models.add(clockModel);
        viewModel.insert(clockModel);

//        selectionTracker = new SelectionTracker.Builder("selction-clock",
//                activityAddalarmBinding.recycler, new StableIdKeyProvider(activityAddalarmBinding.recycler),
//                new MyDetailsLookup(activityAddalarmBinding.recycler), StorageStrategy.createLongStorage()
//                ).build();
//
//        selectionTracker.addObserver(new SelectionTracker.SelectionObserver() {
//            @Override
//            public void onItemStateChanged(@NonNull Object key, boolean selected) {
//                super.onItemStateChanged(key, selected);
//                Log.d(TAG , "selected item key: " + key);
//            }
//        });

        ClockAdapterDiff clockAdapterDiff = new ClockAdapterDiff(getApplicationContext());
        viewModel.getListLiveData().observe(this, clockModels -> {
            clockAdapterDiff.submitList(clockModels);
            Log.d(TAG , "모델추가");
        });

        clockAdapterDiff.setClockClickListener((hour, minute) -> {
            activityAddalarmBinding.timer.setHour(hour);
            activityAddalarmBinding.timer.setMinute(minute);
            revealpm(hour);
        });

        activityAddalarmBinding.recycler.setAdapter(clockAdapterDiff);
        LinearLayoutManager horizontalLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        activityAddalarmBinding.recycler.setLayoutManager(horizontalLayout);

        activityAddalarmBinding.timer.setIs24HourView(true);
        activityAddalarmBinding.timer.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minutes) {
                Log.d(TAG, "시" + hourOfDay + ":" + minutes + "분");

                viewModel.updateHour(clockAdapterDiff.getSelectedID(), hourOfDay, minutes);
                viewModel.updateMinute(clockAdapterDiff.getSelectedID(), hourOfDay, minutes);

                Calendar datetime = Calendar.getInstance();
                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                datetime.set(Calendar.MINUTE, minutes);

                revealpm(hourOfDay);
            }
        });

        activityAddalarmBinding.addSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar tmp = Calendar.getInstance();
                Log.d(TAG, "추가");

                ClockModel tmpClock = new ClockModel(alarmLogic.makeID(tmp.get(Calendar.HOUR_OF_DAY), tmp.get(Calendar.MINUTE), tmp.get(Calendar.SECOND)),
                        tmp.get(Calendar.HOUR_OF_DAY), tmp.get(Calendar.MINUTE), tmp.get(Calendar.HOUR_OF_DAY)<12);
                models.add(tmpClock);
                viewModel.insert(tmpClock);
            }
        });

        activityAddalarmBinding.confirm.setOnClickListener(v -> {

                List<ClockModel> tmpList = viewModel.getListLiveData().getValue();

                for (int i = 0; i<tmpList.size(); i++) {
                    alarmLogic.setToCalendar(tmpList.get(i).getHour(),
                            tmpList.get(i).getMinute(),
                            tmpList.get(i).getHour() >= 12);
                    alarmLogic.newAlarm(tmpList.get(i).getHour() +
                                    tmpList.get(i).getMinute(),
                            alarmLogic.getCalendarTime());
                }
                viewModel.insertAlarm(new AlarmRoom(UUID.randomUUID().toString(), tmpList));
                viewModel.deleteAll();
                super.onBackPressed();

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        viewModel.deleteAll();
    }

    private void revealpm(int hour){

        //am_pm true : 오후 false: 오전
        if( (hour < 12 && !activityAddalarmBinding.pm.isShown())){
            revealThepm();
        } else if (hour >= 12 && activityAddalarmBinding.pm.isShown()) {
            hideThepm();
        }
    }

    private Animator createRevealAnimator(boolean show, View view ) {
        final int cx = view.getWidth() ;
        final int cy = view.getHeight() ;

        final int radius = (int) Math.hypot(cx, cy);
        final Animator animator;

        if (show) {
            animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, radius);
            animator.setInterpolator(new DecelerateInterpolator());
        } else {
            animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, radius, 0);
            animator.setInterpolator(new AccelerateInterpolator());
        }

        animator.setDuration(getResources().getInteger(R.integer.default_anim_duration));
        return animator;
    }


    private void revealThepm() {
        activityAddalarmBinding.pm.setVisibility(View.VISIBLE);
        Animator reveal = createRevealAnimator(true, activityAddalarmBinding.pm);
        reveal.start();
    }

    /**
     * Hide the am
     */
    private void hideThepm() {
        Animator hide = createRevealAnimator(false, activityAddalarmBinding.pm);
        hide.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                activityAddalarmBinding.pm.setVisibility(View.INVISIBLE);
            }
        });
        hide.start();
    }

}
