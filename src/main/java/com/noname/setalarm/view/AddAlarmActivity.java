package com.noname.setalarm.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
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
    private ClockViewModel viewModel;
    private SelectionTracker selectionTracker;
    private ArrayList<ClockModel> models = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityAddalarmBinding = DataBindingUtil.setContentView(this, R.layout.activity_addalarm);
        activityAddalarmBinding.setLifecycleOwner(this);
        activityAddalarmBinding.setProvider(new ImageProvider(R.drawable.hills, R.drawable.moon));
        setSupportActionBar(activityAddalarmBinding.toolbar);
        Intent intent = getIntent();

        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) >= 12){
            activityAddalarmBinding.pm.setVisibility(View.INVISIBLE);
        }else {
            activityAddalarmBinding.pm.setVisibility(View.VISIBLE);
        }

        alarmLogic = new AlarmLogic(this);
        viewModel = ViewModelProviders.of(this).get(ClockViewModel.class);

        if (intent.hasExtra("modifyList")) {

            models = intent.getParcelableArrayListExtra("modifyList");

            for (ClockModel clockModel : models){
                viewModel.insert(clockModel);
            }

        }else {
//        Log.d(TAG, "HOUR + " + alarmLogic.getCurrentHour() + "MINUTE : " + alarmLogic.getCurrentMinute());
            ClockModel clockModel = new ClockModel(alarmLogic.makeID(alarmLogic.getCurrentHour(), alarmLogic.getCurrentMinute(), alarmLogic.getCurrentSecond()),
                    alarmLogic.getCurrentHour(), alarmLogic.getCurrentMinute(), alarmLogic.getCurrentHour() >= 12);
            models.add(clockModel);
            viewModel.insert(clockModel);
        }
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

        ClockAdapter clockAdapter = new ClockAdapter(getApplicationContext());
        viewModel.getListLiveData().observe(this, clockModels -> {
            clockAdapter.submitList(clockModels);
        });

        clockAdapter.setClockClickListener((hour, minute) -> {
            activityAddalarmBinding.timer.setHour(hour);
            activityAddalarmBinding.timer.setMinute(minute);
            revealpm(hour);
        });

        activityAddalarmBinding.recycler.setAdapter(clockAdapter);
        LinearLayoutManager horizontalLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        activityAddalarmBinding.recycler.setLayoutManager(horizontalLayout);

        activityAddalarmBinding.timer.setIs24HourView(true);
        activityAddalarmBinding.timer.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minutes) {

                viewModel.updateHour(clockAdapter.getSelectedID(), hourOfDay);
                viewModel.updateMinute(clockAdapter.getSelectedID(),  minutes);
                viewModel.updateAMPM(clockAdapter.getSelectedID(),  hourOfDay >= 12);

                revealpm(hourOfDay);
            }
        });

        ArrayList<ClockModel> finalModels = models;
        activityAddalarmBinding.addSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar tmp = Calendar.getInstance();
                Log.d(TAG, "추가");

                ClockModel tmpClock = new ClockModel(alarmLogic.makeID(tmp.get(Calendar.HOUR_OF_DAY), tmp.get(Calendar.MINUTE), tmp.get(Calendar.SECOND)),
                        tmp.get(Calendar.HOUR_OF_DAY), tmp.get(Calendar.MINUTE), tmp.get(Calendar.HOUR_OF_DAY)<12);
                finalModels.add(tmpClock);
                viewModel.insert(tmpClock);
            }
        });

        activityAddalarmBinding.confirm.setOnClickListener(v -> {
            //수정버튼으로 들어온 경우
            //원래 id 의 alarmroom 데이터를 수정된 clocklist로 덮어씌워야함.
            //원래 id의 알람은 삭제.
            if(intent.hasExtra("modifyList")){

                for (int i = 0; i<models.size(); i++) {
                    alarmLogic.unregisterAlarm(models.get(i).getId());
                }

                List<ClockModel> tmpList = viewModel.getListLiveData().getValue();

                for (int i = 0; i < tmpList.size(); i++) {

                    alarmLogic.setToCalendar(tmpList.get(i).getHour(),
                            tmpList.get(i).getMinute(),
                            tmpList.get(i).getHour() >= 12);

                    alarmLogic.newAlarm(tmpList.get(i).getId(),
                            alarmLogic.getCalendarTime());
                }
                viewModel.updateAlarm(intent.getStringExtra("alarmId"), tmpList);

            }else {
                List<ClockModel> tmpList = viewModel.getListLiveData().getValue();

                for (int i = 0; i < tmpList.size(); i++) {

                    alarmLogic.setToCalendar(tmpList.get(i).getHour(),
                            tmpList.get(i).getMinute(),
                            tmpList.get(i).getHour() >= 12);

                    alarmLogic.newAlarm(tmpList.get(i).getId(),
                            alarmLogic.getCalendarTime());
                }

                viewModel.insertAlarm(new AlarmRoom(UUID.randomUUID().toString(), tmpList, true,
                        activityAddalarmBinding.memo.getText().toString()));
            }

            viewModel.deleteAll();
            super.onBackPressed();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        rescaleViewAnimation(activityAddalarmBinding.confirm, 1f, 500, null);

    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public void finish(){

        rescaleViewAnimation(activityAddalarmBinding.confirm, 0, 300, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                AddAlarmActivity.super.finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
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
        activityAddalarmBinding.am.setVisibility(View.INVISIBLE);
    }

    /**
     * Hide the am
     */
    private void hideThepm() {
        activityAddalarmBinding.am.setVisibility(View.VISIBLE);

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

    private void rescaleViewAnimation(View view,
                                      float targetScale,
                                      int durationOffset,
                                      Animator.AnimatorListener listener) {
        ObjectAnimator scaleOnX = ObjectAnimator.ofFloat(view, "scaleX", targetScale);
        ObjectAnimator scaleOnY = ObjectAnimator.ofFloat(view, "scaleY", targetScale);
        scaleOnX.setDuration(durationOffset);
        scaleOnY.setDuration(durationOffset);

        AnimatorSet scaleSet = new AnimatorSet();
        scaleSet.playTogether(
                scaleOnX,
                scaleOnY
        );
        if (listener != null){
            scaleSet.addListener(listener);
        }

        scaleSet.start();
    }
}
