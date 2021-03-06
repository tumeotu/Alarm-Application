package com.android.alarm.Models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AlarmDao {
    @Insert
    void insert(Alarm alarm);

    @Delete
    void delete(Alarm alarm);

    @Query("SELECT * FROM alarm_table ORDER BY created ASC")
    LiveData<List<Alarm>> getAlarms();

    @Query("SELECT * FROM alarm_table WHERE alarmId == :alarmID ORDER BY created ASC")
    LiveData<List<Alarm>> getAlarm(int alarmID);

    @Update
    void update(Alarm alarm);
}
