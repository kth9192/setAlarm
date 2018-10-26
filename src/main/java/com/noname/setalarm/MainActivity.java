package com.noname.setalarm;

import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;


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

                Intent intent = new Intent(getApplicationContext(), AddAlarmActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(MainActivity.this, view, "toAddAlarm");
                startActivity(intent, options.toBundle());

            }
        });

        AlarmAdpater alarmAdpater = new AlarmAdpater(this, alarmRoomViewModel);
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
                            return Integer.compare(newData.getId(), oldData.getId());
                        }
                    });
                }
                alarmAdpater.submitList(alarmRooms);
            }
        });

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
