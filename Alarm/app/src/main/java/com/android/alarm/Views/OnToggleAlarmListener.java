package com.android.alarm.Views;


import com.android.alarm.Models.Alarm;

public interface OnToggleAlarmListener {
    void onToggle(Alarm alarm);
    void onClick(Alarm alarm);
    void onLongClick(Alarm alarm);
}
