package com.example.techdash.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.techdash.R;
import com.example.techdash.models.History;
import com.example.techdash.repositories.RecordRunRepository;
import com.example.techdash.viewmodels.UserViewModel;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private static final String TAG = HistoryFragment.class.getSimpleName();
    private UserViewModel userViewModel;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        String uid = null;
        if (userViewModel.getUser().getValue() == null)
            uid = "X5P3gKcD6CUETVxiXIDDZ4arNwh2";
        else
            uid = userViewModel.getUser().getValue().getUid();

        RecordRunRepository.getInstance().fetch(uid).observe(getViewLifecycleOwner(), new Observer<ArrayList<History>>() {
            @Override
            public void onChanged(ArrayList<History> histories) {
                Log.d(TAG, "history size: " + histories.size());
            }
        });
        return v;
    }
}