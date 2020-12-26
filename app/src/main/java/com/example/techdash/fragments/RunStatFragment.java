package com.example.techdash.fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.techdash.R;
import com.example.techdash.models.Route;
import com.example.techdash.viewmodels.RecordViewModel;

public class RunStatFragment extends Fragment {

    private Button mapButton;
    private NavController navController;
    private TextView textViewDistance;
    private TextView textViewPace;
    private RecordViewModel recordViewModel;
    private Chronometer chronometer;

    public RunStatFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_run_stat, container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.fragment);
        mapButton = v.findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
                navController.navigate(R.id.runMapFragment);
            }
        });

        recordViewModel = new ViewModelProvider(requireActivity()).get(RecordViewModel.class);
        textViewDistance = v.findViewById(R.id.tvDistance);
        recordViewModel.getDistance().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double distance) {
                textViewDistance.setText(String.format("%.2f km", distance));
            }
        });

        recordViewModel.getRoute().observe(getViewLifecycleOwner(), new Observer<Route>() {
            @Override
            public void onChanged(Route route) {
                recordViewModel.storeRoute(route);
            }
        });

        textViewPace = v.findViewById(R.id.tvPace);
        recordViewModel.getPace().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double pace) {
                textViewPace.setText(String.format("%.1f /km", pace));
            }
        });

        chronometer = (Chronometer) v.findViewById(R.id.chronoTime);
        if (recordViewModel.getStartTime()==null){
            long startTime = SystemClock.elapsedRealtime();
            recordViewModel.setStartTime(startTime);
            chronometer.setBase(startTime);
        } else {
            // Otherwise set the chronometer's base to the original
            // starting time.
            chronometer.setBase(recordViewModel.getStartTime());

        }
        chronometer.start();
        chronometer.setOnChronometerTickListener(new OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                Log.d("timmmm","a");
            }
        });
        return v;
    }

}
