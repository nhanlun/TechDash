package com.example.techdash.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.techdash.R;
import com.example.techdash.models.History;
import com.example.techdash.repositories.RecordRunRepository;
import com.example.techdash.viewmodels.UserViewModel;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

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
        String uid = userViewModel.getUser().getValue().getUid();
        ArrayList<History> histories = RecordRunRepository.getInstance().fetch(uid);
        return v;
    }
}