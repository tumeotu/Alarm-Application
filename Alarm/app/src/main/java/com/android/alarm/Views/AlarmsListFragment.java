package com.android.alarm.Views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.alarm.Apdapters.AlarmRecyclerViewAdapter;
import com.android.alarm.Models.Alarm;
import com.android.alarm.R;
import com.android.alarm.ViewModels.AlarmsListViewModel;
import com.android.alarm.ViewModels.CreateAlarmViewModel;
import com.android.alarm.databinding.FragmentListalarmsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AlarmsListFragment extends Fragment implements OnToggleAlarmListener {
    private AlarmRecyclerViewAdapter alarmRecyclerViewAdapter;
    private AlarmsListViewModel alarmsListViewModel;
    private RecyclerView alarmsRecyclerView;
    private FloatingActionButton addAlarm;
    private FragmentListalarmsBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alarmRecyclerViewAdapter = new AlarmRecyclerViewAdapter(this);
        alarmsListViewModel = new ViewModelProvider(requireActivity()).get(AlarmsListViewModel.class);
        alarmsListViewModel.getAlarmsLiveData().observe(this, new Observer<List<Alarm>>() {
            @Override
            public void onChanged(List<Alarm> alarms) {
                if (alarms != null) {
                    alarmRecyclerViewAdapter.setAlarms(alarms);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListalarmsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alarmsRecyclerView = binding.fragmentListalarmsRecylerView;
        alarmsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        alarmsRecyclerView.setAdapter(alarmRecyclerViewAdapter);
        addAlarm = binding.fragmentListalarmsAddAlarm;
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_alarmsListFragment_to_createAlarmFragment);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onToggle(Alarm alarm) {
        if (alarm.isStarted()) {
            alarm.cancelAlarm(getContext());
            alarmsListViewModel.update(alarm);
        } else {
            alarm.schedule(getContext());
            alarmsListViewModel.update(alarm);
        }
    }

    @Override
    public void onClick(Alarm alarm) {
        CreateAlarmViewModel createAlarmViewModel =
                new ViewModelProvider(requireActivity()).get(CreateAlarmViewModel.class);
        createAlarmViewModel.setAlarm(alarm);
        NavHostFragment.findNavController(this).navigate(R.id.action_alarmsListFragment_to_createAlarmFragment);
    }

    @Override
    public void onLongClick(Alarm alarm) {
        @SuppressLint("DefaultLocale") String alarmTime = String.format("%02d:%02d", alarm.getHour(), alarm.getMinute());
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Delete Alarm")
                .setMessage("Do you want to delete alarm at "+alarmTime)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                alarm.cancelAlarm(getContext());
                                alarmsListViewModel.update(alarm);
                                alarmsListViewModel.delete(alarm);
                            }
                        }
                )
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {}
                        }
                )
                .create();
        dialog.show();
    }
}