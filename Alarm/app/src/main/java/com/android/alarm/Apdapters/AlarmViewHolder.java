package com.android.alarm.Apdapters;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.alarm.Models.Alarm;
import com.android.alarm.R;
import com.android.alarm.Views.OnToggleAlarmListener;

public class AlarmViewHolder extends RecyclerView.ViewHolder {
    private TextView alarmTime;
    private TextView alarmTitle;
    private TextView alarmRecurringDays;
    Switch alarmStarted;

    private OnToggleAlarmListener listener;

    public AlarmViewHolder(@NonNull View itemView, OnToggleAlarmListener listener) {
        super(itemView);

        alarmTime = itemView.findViewById(R.id.item_alarm_time);
        alarmStarted = itemView.findViewById(R.id.item_alarm_started);
        alarmTitle = itemView.findViewById(R.id.item_alarm_title);
        alarmRecurringDays = itemView.findViewById(R.id.item_alarm_recurringDays);
        this.listener = listener;
    }

    public void bind(final Alarm alarm) {
        @SuppressLint("DefaultLocale") String alarmText = String.format("%02d:%02d", alarm.getHour(), alarm.getMinute());
        alarmTime.setText(alarmText);
        alarmStarted.setChecked(alarm.isStarted());
        alarmTitle.setText(alarm.getTitle());
        if (alarm.isRecurring()) {
            alarmRecurringDays.setText(alarm.getRecurringDaysText());
        } else {
            alarmRecurringDays.setText("Once Off");
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(alarm);
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(alarm);
                return true;
            }
        });

        alarmStarted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.onToggle(alarm);
            }
        });
    }
}
