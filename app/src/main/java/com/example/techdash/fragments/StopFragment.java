package com.example.techdash.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.techdash.R;
import com.example.techdash.services.RecordService;
import com.example.techdash.viewmodels.RecordViewModel;
import com.google.android.material.button.MaterialButton;

public class StopFragment extends Fragment {
    private MaterialButton stopButton;
    private RecordViewModel recordViewModel;

    public StopFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stop, container, false);
        recordViewModel = new ViewModelProvider(requireActivity()).get(RecordViewModel.class);
        stopButton = v.findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordViewModel.stopRecording();
            }
        });
        return v;
    }
}