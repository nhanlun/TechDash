package com.example.techdash.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
import com.example.techdash.models.Contest;
import com.example.techdash.viewmodels.ContestViewModel;
import com.example.techdash.viewmodels.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class ContestAddFragment extends Fragment {
    final static String TAG = ContestAddFragment.class.getSimpleName();
    EditText edtName, edtMember, destination;
    Button btnSave;
    TextView starttime, endtime, creator;
    boolean inputE = false, inputS = false;
    String name;
    Calendar start, end;
    ContestViewModel contestViewModel;
    UserViewModel userViewModel;
    ArrayList<String> participants = new ArrayList<String>();

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
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        start.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        start.set(Calendar.MINUTE, minute);
                        SimpleDateFormat sf = new SimpleDateFormat("EEE, dd-MM-yyyy, HH:mm", Locale.ENGLISH);
                        starttime.setText(sf.format(start.getTime()));
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
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        end.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        end.set(Calendar.MINUTE, minute);
                        SimpleDateFormat sf = new SimpleDateFormat("EEE, dd-MM-yyyy, HH:mm", Locale.ENGLISH);
                        endtime.setText(sf.format(end.getTime()));
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
        return true;
    }

    public static String randomString() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        char tempChar;
        for (int i = 0; i < 16; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final NavController navController = Navigation.findNavController(view);
        btnSave = getView().findViewById(R.id.ct_save);
        edtName = getView().findViewById(R.id.ct_name);
        starttime = getView().findViewById(R.id.ct_starttime);
        endtime = getView().findViewById(R.id.ct_endtime);
        creator = getView().findViewById(R.id.ct_creator);
        destination = getView().findViewById(R.id.ct_destination);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        creator.setText(String.format("Created by %s", userViewModel.getUser().getValue().getName()));

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
                contestViewModel = new ViewModelProvider(requireActivity()).get(ContestViewModel.class);
                if (checkSave()) {
                    String id = randomString();
                    participants.add(userViewModel.getUser().getValue().getUid());
                    String dest = destination.getText().toString();
                    String username = userViewModel.getUser().getValue().getName();
                    String startt = String.valueOf(start.getTimeInMillis());
                    String endd = String.valueOf(end.getTimeInMillis());

                    contestViewModel.save(new Contest(id, dest, name, username, startt, endd, participants));
                    navController.popBackStack();
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}