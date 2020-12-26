package com.example.techdash.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techdash.R;
import com.example.techdash.adapters.HistoryAdapter;
import com.example.techdash.models.History;
import com.example.techdash.repositories.RecordRunRepository;
import com.example.techdash.viewmodels.UserViewModel;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private UserViewModel userViewModel;
    RecyclerView recyclerView;
    HistoryAdapter historyAdapter;
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
        uid = "X5P3gKcD6CUETVxiXIDDZ4arNwh2";
//        if (uid == null){
//            uid = "X5P3gKcD6CUETVxiXIDDZ4arNwh2";
//        }
        ArrayList<History> histories = RecordRunRepository.getInstance().fetch(uid);

        Log.d("AAA","getRV");
        Log.d("AAA", String.valueOf(histories.size()));
        recyclerView = v.findViewById(R.id.rvHistory);
        historyAdapter = new HistoryAdapter(histories,getContext(),R.layout.fragment_history_row);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(historyAdapter);

        return v;
    }
}