package com.example.techdash.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.techdash.R;
import com.example.techdash.viewmodels.RecordViewModel;
import com.google.android.material.button.MaterialButton;

public class FinishFragment extends Fragment {
    private RecordViewModel recordViewModel;
    private MaterialButton finishButton;

    public FinishFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recordViewModel = new ViewModelProvider(requireActivity()).get(RecordViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_finish, container, false);
        finishButton = v.findViewById(R.id.finishButon);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: save record
                requireActivity().finish();
            }
        });

        // TODO: observe data
        return v;
    }
}