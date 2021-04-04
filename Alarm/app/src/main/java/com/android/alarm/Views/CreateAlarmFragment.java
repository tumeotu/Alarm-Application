package com.android.alarm.Views;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.android.alarm.Models.Alarm;
import com.android.alarm.R;
import com.android.alarm.Utils.TimePickerUtil;
import com.android.alarm.ViewModels.CreateAlarmViewModel;

import java.util.Random;
import com.android.alarm.databinding.FragmentCreatealarmBinding;
import com.google.android.material.textfield.TextInputLayout;

public class CreateAlarmFragment extends Fragment {

    FragmentCreatealarmBinding binding;
    TimePicker timePicker;
    TextInputLayout title;
    CheckBox recurring;
    CheckBox mon;
    CheckBox tue;
    CheckBox wed;
    CheckBox thu;
    CheckBox fri;
    CheckBox sat;
    CheckBox sun;
    LinearLayout recurringOptions;
    LinearLayout recurringOptions_next;
    ImageView addAlarm;
    ImageView cancelAlarm;


    private CreateAlarmViewModel createAlarmViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createAlarmViewModel = new ViewModelProvider(requireActivity()).get(CreateAlarmViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreatealarmBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        timePicker = binding.fragmentCreatealarmTimePicker;
        title= binding.fragmentCreatealarmTitle;
        recurring= binding.fragmentCreatealarmRecurring;
        mon= binding.fragmentCreatealarmCheckMon;
        tue= binding.fragmentCreatealarmCheckTue;
        wed= binding.fragmentCreatealarmCheckWed;
        thu= binding.fragmentCreatealarmCheckThu;
        fri=binding.fragmentCreatealarmCheckFri;
        sat=binding.fragmentCreatealarmCheckSat;
        sun=binding.fragmentCreatealarmCheckSun;
        recurringOptions= binding.fragmentCreatealarmRecurringOptions;
        recurringOptions_next= binding.fragmentCreatealarmRecurringOptionsNext;
        addAlarm = binding.AddAlarm;
        cancelAlarm= binding.cancelAlarm;

        if(createAlarmViewModel.getAlarm()!=null){
            setViewUpdateAlarm(createAlarmViewModel.getAlarm());
        }

        timePicker.setIs24HourView(true);
        cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlarmViewModel.setAlarm(null);
                Navigation.findNavController(view).navigate(R.id.action_createAlarmFragment_to_alarmsListFragment);
            }
        });

        recurring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showRecurring(isChecked);
            }
        });

        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(createAlarmViewModel.getAlarm()!=null)
                    updateAlarm(createAlarmViewModel.getAlarm());
                else
                    scheduleAlarm();
                createAlarmViewModel.setAlarm(null);
                Navigation.findNavController(v).navigate(R.id.action_createAlarmFragment_to_alarmsListFragment);
            }
        });
    }

    private void showRecurring(boolean isShow)
    {
        if (isShow) {
            recurringOptions.setVisibility(View.VISIBLE);
            recurringOptions_next.setVisibility(View.VISIBLE);
        } else {
            recurringOptions.setVisibility(View.GONE);
            recurringOptions_next.setVisibility(View.GONE);
        }
    }
    private void setViewUpdateAlarm(Alarm alarm)
    {
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.update);
        addAlarm.setImageBitmap(icon);
        showRecurring(alarm.isRecurring());
        mon.setChecked(alarm.isMonday());
        tue.setChecked(alarm.isTuesday());
        wed.setChecked(alarm.isWednesday());
        thu.setChecked(alarm.isThursday());
        fri.setChecked(alarm.isFriday());
        sat.setChecked(alarm.isSaturday());
        sun.setChecked(alarm.isSunday());
        title.getEditText().setText(alarm.getTitle());
        timePicker.setHour(alarm.getHour());
        timePicker.setMinute(alarm.getMinute());
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void scheduleAlarm() {
        int alarmId = new Random().nextInt(Integer.MAX_VALUE);
        Alarm alarm = new Alarm(
                alarmId,
                TimePickerUtil.getTimePickerHour(timePicker),
                TimePickerUtil.getTimePickerMinute(timePicker),
                title.getEditText().getText().toString(),
                System.currentTimeMillis(),
                true,
                recurring.isChecked(),
                mon.isChecked(),
                tue.isChecked(),
                wed.isChecked(),
                thu.isChecked(),
                fri.isChecked(),
                sat.isChecked(),
                sun.isChecked()
        );
        createAlarmViewModel.insert(alarm);
        alarm.schedule(getContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateAlarm(Alarm alarmUpdate) {
        Alarm alarm = new Alarm(
                alarmUpdate.getAlarmId(),
                TimePickerUtil.getTimePickerHour(timePicker),
                TimePickerUtil.getTimePickerMinute(timePicker),
                title.getEditText().getText().toString(),
                System.currentTimeMillis(),
                true,
                recurring.isChecked(),
                mon.isChecked(),
                tue.isChecked(),
                wed.isChecked(),
                thu.isChecked(),
                fri.isChecked(),
                sat.isChecked(),
                sun.isChecked()
        );
        createAlarmViewModel.update(alarm);
        alarm.schedule(getContext());
    }
}
