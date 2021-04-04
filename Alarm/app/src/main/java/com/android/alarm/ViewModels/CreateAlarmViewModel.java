package com.android.alarm.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.alarm.Models.Alarm;
import com.android.alarm.Models.AlarmRepository;

import java.util.List;

public class CreateAlarmViewModel extends AndroidViewModel {
    private AlarmRepository alarmRepository;
    private MutableLiveData<Alarm> selectedAlarm = new MutableLiveData<>();

    public CreateAlarmViewModel(@NonNull Application application) {
        super(application);
        alarmRepository = new AlarmRepository(application);
    }
    public void insert(Alarm alarm) {
        alarmRepository.insert(alarm);
    }

    public void setAlarm(Alarm alarm){
        this.selectedAlarm.postValue(alarm);
    }
    public Alarm getAlarm(){
        return this.selectedAlarm.getValue();
    }

    public void update(Alarm alarm) {
        alarmRepository.update(alarm);
    }
}
