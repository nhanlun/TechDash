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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.techdash.R;

import java.util.Calendar;

public class ContestAddFragment extends Fragment {
    EditText edtName, edtMember;
    Button btnSave;
    TextView starttime, endtime;
    boolean inputE = false, inputS = false;
    final static String TAG = HomeFragment.class.getSimpleName();
    String time = "", name, member;
    Calendar start, end;

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
                        inputS = true;
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
                        inputE = true;
                        Log.v(TAG, "The choosen one " + end.getTime());
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    private boolean checkSave() {
        if (name == null || name.equals("")) {
            Toast.makeText(getActivity(), "Please input Contest name", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!inputS) {
            Toast.makeText(getActivity(), "Please input Start time", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!inputE) {
            Toast.makeText(getActivity(), "Please input End time", Toast.LENGTH_LONG).show();
            return false;
        }
        if (end.before(start)) {
            Toast.makeText(getActivity(), "End time must be after Start time", Toast.LENGTH_LONG).show();
            return false;
        }
        if (member == null || member.equals("")) {
            Toast.makeText(getActivity(), "Please input some members", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void SaveMember(String mem) {
        String tmp = "";
        for (int i = 0; i < mem.length(); i++) {
            if (mem.charAt(i) != ' ') {
                tmp = tmp + mem.charAt(i);
            } else {
                //save vao list member
                tmp = "";
            }
        }
        if (tmp != null && !tmp.equals("")) {
            //save vao list member
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnSave = getView().findViewById(R.id.ct_save);
        edtName = getView().findViewById(R.id.ct_name);
        starttime = getView().findViewById(R.id.ct_starttime);
        endtime = getView().findViewById(R.id.ct_endtime);
        edtMember = getView().findViewById(R.id.ct_member);

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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edtName.getText().toString();
                member = edtMember.getText().toString();
                if (checkSave()) {
                    //SAVE
                    SaveMember(member);
                }
            }
        });
    }
}