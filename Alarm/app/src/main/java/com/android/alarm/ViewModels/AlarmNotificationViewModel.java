package com.android.alarm.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.android.alarm.Models.Alarm;
import com.android.alarm.Models.AlarmRepository;

import java.util.List;

public class AlarmNotificationViewModel extends AndroidViewModel {
    private AlarmRepository alarmRepository;
    private LiveData<Alarm> alarmLiveData;
    public AlarmNotificationViewModel(@NonNull Application application) {
        super(application);
        alarmRepository = new AlarmRepository(application);
    }

    public LiveData<List<Alarm>> getAlarmLiveData(int alarmID) {
        return alarmRepository.getAlarmLiveData(alarmID);
    }

    public void update(Alarm alarm) {
        alarmRepository.update(alarm);
    }


}
