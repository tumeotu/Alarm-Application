package com.android.alarm.Views;


import android.app.ActivityManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.android.alarm.Models.Alarm;
import com.android.alarm.R;
import com.android.alarm.Service.AlarmService;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import com.android.alarm.ViewModels.AlarmNotificationViewModel;
import com.android.alarm.activities.MainActivity;
import com.android.alarm.application.App;

public class AlarmNotificationActivity extends AppCompatActivity {
    Button dismiss;
    Button snooze;
    ImageView clock;
    Alarm alarmNotify=null;
    AlarmNotificationViewModel alarmNotificationViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_notification_fragment);
        int alarmID = App.alarmID;
        alarmNotificationViewModel = new ViewModelProvider(AlarmNotificationActivity.this).get(AlarmNotificationViewModel.class);
        alarmNotificationViewModel.getAlarmLiveData(alarmID).observe(AlarmNotificationActivity.this, new Observer<List<Alarm>>() {
            @Override
            public void onChanged(List<Alarm> alarms) {
                if (alarms != null) {
                    for(Alarm item: alarms)
                    {
                        if(item.getAlarmId()==alarmID)
                        {
                            alarmNotify= item;
                            break;
                        }
                    }
                }
            }
        });
        dismiss= findViewById(R.id.activity_ring_dismiss);
        snooze= findViewById(R.id.activity_ring_snooze);
        clock= findViewById(R.id.activity_ring_clock);

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alarmNotify!=null)
                {
                    alarmNotify.cancelAlarm(AlarmNotificationActivity.this);
                    alarmNotificationViewModel.update(alarmNotify);
                }
                Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
                getApplicationContext().stopService(intentService);
                isSingleNotifyActivity();
                finish();
            }
        });
        snooze.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.add(Calendar.MINUTE, 10);

                Alarm alarm = new Alarm(
                        new Random().nextInt(Integer.MAX_VALUE),
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        "Snooze",
                        System.currentTimeMillis(),
                        true,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false,
                        false
                );
                alarm.schedule(getApplicationContext());
                Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
                getApplicationContext().stopService(intentService);
                finish();
                isSingleNotifyActivity();
            }
        });
    }

    private void isSingleNotifyActivity()
    {
        ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );

        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

        if(taskList.get(0).numActivities == 1) {
            Intent intent = new Intent(AlarmNotificationActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
