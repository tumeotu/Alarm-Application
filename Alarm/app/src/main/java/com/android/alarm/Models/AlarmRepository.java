package com.android.alarm.Models;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AlarmRepository {
    private AlarmDao alarmDao;
    private LiveData<List<Alarm>> alarmsLiveData;

    public AlarmRepository(Application application) {
        AlarmDatabase db = AlarmDatabase.getDatabase(application);
        alarmDao = db.alarmDao();
        alarmsLiveData = alarmDao.getAlarms();
    }

    public void insert(final Alarm alarm) {
        AlarmDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                alarmDao.insert(alarm);
            }
        });
    }

    public void update(final Alarm alarm) {
        AlarmDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                alarmDao.update(alarm);
            }
        });
    }

    public void detele(final Alarm alarm) {
        AlarmDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                alarmDao.delete(alarm);
            }
        });
    }

    public LiveData<List<Alarm>> getAlarmsLiveData() {
        return alarmsLiveData;
    }

    public LiveData<List<Alarm>> getAlarmLiveData(int alarmID) {
        return alarmDao.getAlarm(alarmID);
    }
}
