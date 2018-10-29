package com.noname.setalarm;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


import com.noname.setalarm.databinding.ActivityMainBinding;
import com.noname.setalarm.model.ClockModel;
import com.noname.setalarm.repository.AlarmRoom;
import com.noname.setalarm.view.AddAlarmActivity;
import com.noname.setalarm.view.AlarmAdpater;
import com.noname.setalarm.viewmodel.AlarmRoomViewModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding activityMainBinding;
    private AlarmRoomViewModel alarmRoomViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        alarmRoomViewModel = ViewModelProviders.of(this).get(AlarmRoomViewModel.class);
        activityMainBinding.setLifecycleOwner(this);

        activityMainBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rescaleViewAnimation(activityMainBinding.fab, 0, 300, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        activityMainBinding.fab.setClickable(false);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Intent intent = new Intent(getApplicationContext(), AddAlarmActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        });

        AlarmAdpater alarmAdpater = new AlarmAdpater(this, alarmRoomViewModel);
        alarmAdpater.setHasStableIds(true);
        activityMainBinding.recycler.setAdapter(alarmAdpater);
        activityMainBinding.recycler.setLayoutManager(new LinearLayoutManager(this));

        alarmRoomViewModel.getListLiveData().observe(this, new Observer<List<AlarmRoom>>() {
            @Override
            public void onChanged(@Nullable List<AlarmRoom> alarmRooms) {
                for (AlarmRoom alarmRoom : alarmRooms) {
                    Collections.sort(alarmRoom.getTimeList() , new Comparator<ClockModel>() {

                        @Override
                        public int compare(ClockModel oldData, ClockModel newData) {
                            // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
//
//                            Log.d(TAG, "전데이터 " + oldData.getId());
//                            Log.d(TAG, "후데이터 " + newData.getId());

                            return Integer.compare(oldData.getId(), newData.getId());
                        }
                    });
                }
                alarmAdpater.submitList(alarmRooms);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        activityMainBinding.fab.setClickable(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        rescaleViewAnimation(activityMainBinding.fab, 1f, 300, null);

//        activityMainBinding.fab.animate()
//                .scaleX(1)
//                .scaleY(1)
//                .setInterpolator(new FastOutSlowInInterpolator())
//                .setStartDelay(200)
//                .setListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) { }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) { }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animation) { }
//                })
//                .start();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
