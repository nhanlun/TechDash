package com.example.techdash.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.techdash.R;

import java.util.Calendar;

public class ContestAddFragment extends Fragment {
    TextView starttime, endtime;
    final static String TAG = HomeFragment.class.getSimpleName();
    String time = "";
    Calendar start, end;
    int selectedYear, selectedMonth, selectedDayOfMonth, lastSelectedHour, lastSelectedMinute;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contest_add, container, false);
    }

    public void StartTime() {
        final Calendar currentDate = Calendar.getInstance();
        start = Calendar.getInstance();
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                start.set(year, monthOfYear, dayOfMonth);
                time = String.valueOf(dayOfMonth) + '-' + (monthOfYear + 1) + '-' + year;
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        start.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        start.set(Calendar.MINUTE, minute);
                        if (minute > 10) time = time + ' ' + hourOfDay + ':' + minute;
                        else time = time + ' ' + hourOfDay + ":0" + minute;
                        starttime.setText(time);
                        Log.v(TAG, "The choosen one " + start.getTime());
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    public void EndTime() {
        final Calendar currentDate = Calendar.getInstance();
        end = Calendar.getInstance();
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                end.set(year, monthOfYear, dayOfMonth);
                time = String.valueOf(dayOfMonth) + '-' + (monthOfYear + 1) + '-' + year;
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        end.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        end.set(Calendar.MINUTE, minute);
                        if (minute > 10) time = time + ' ' + hourOfDay + ':' + minute;
                        else time = time + ' ' + hourOfDay + ":0" + minute;
                        endtime.setText(time);
                        Log.v(TAG, "The choosen one " + end.getTime());
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        starttime = getView().findViewById(R.id.ct_starttime);
        endtime = getView().findViewById(R.id.ct_endtime);

        starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTime();

            }
        });

        endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndTime();
            }
        });
    }
}