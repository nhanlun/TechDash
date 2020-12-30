package com.example.techdash.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techdash.R;
import com.example.techdash.adapters.HistoryAdapter;
import com.example.techdash.models.History;
import com.example.techdash.repositories.RecordRunRepository;
import com.example.techdash.viewmodels.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private static final String TAG = HistoryFragment.class.getSimpleName();
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

        if (uid != null) {

            ArrayList<History> historyArrayList = new ArrayList<>();

            recyclerView = v.findViewById(R.id.rvHistory);
            historyAdapter = new HistoryAdapter(historyArrayList, getContext(), R.layout.fragment_history_row);

            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(historyAdapter);


            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    FloatingActionButton fab = getActivity().findViewById(R.id.runButton);
                    if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                        fab.hide();
                    } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                        fab.show();
                    }
                }
            });

            RecordRunRepository.getInstance().fetch(uid).observe(getViewLifecycleOwner(), new Observer<ArrayList<History>>() {
                @Override
                public void onChanged(ArrayList<History> histories) {
                    historyAdapter.setHistoryArrayList(histories);
                    historyAdapter.notifyDataSetChanged();

                }
            });
        }
        return v;
    }
}