package com.example.techdash.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techdash.R;
import com.example.techdash.adapters.ContestAdapter;
import com.example.techdash.models.Contest;
import com.example.techdash.models.User;
import com.example.techdash.viewmodels.ContestViewModel;
import com.example.techdash.viewmodels.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

public class ContestFragment extends Fragment {
    RecyclerView recyclerView;
    private static final String TAG = ContestFragment.class.getSimpleName();
    View v;
    boolean showEndedContests = true, showOnlyUserContests = false;
    ArrayList<Contest> contestList = new ArrayList<>();
    ContestAdapter contestAdapter;
    private UserViewModel userViewModel;
    private ContestViewModel contestViewModel;
    private ImageButton addContestBtn;

    public ContestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_contest, container, false);
            Log.d(TAG, "New view inflated");
        }

        recyclerView = v.findViewById(R.id.rvContest);
        contestAdapter = new ContestAdapter(contestList, getContext(), R.layout.fragment_contest_row);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(contestAdapter);

        addContestBtn = v.findViewById(R.id.addContestBtn);
        addContestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.contestAddFragment);
            }
        });

        contestViewModel = new ViewModelProvider(requireActivity()).get(ContestViewModel.class);
        contestViewModel.getContests().observe(getViewLifecycleOwner(), new Observer<ArrayList<Contest>>() {
            @Override
            public void onChanged(ArrayList<Contest> contests) {
                contestAdapter.setContestArrayList(contests);
                contestAdapter.notifyDataSetChanged();
            }
        });
        return v;
    }

    ArrayList<Contest> preFilter() {
        if (showEndedContests && !showOnlyUserContests) return contestList;
        ArrayList<Contest> tempList = new ArrayList<Contest>();
        String currentDate = Calendar.getInstance().getTime().toString();
        String uid = userViewModel.getUser().getValue().getUid();

        if(contestList != null) {
            int length = contestList.size();
            int i=0;
            while(i < length){
                Contest item = contestList.get(i);
                if ((showEndedContests || currentDate.compareTo(item.getEndTime()) < 0) && (!showOnlyUserContests || item.checkParticipant(uid)))
                    tempList.add(item);
                i++;
            }
        }
        return tempList;
    }

    ArrayList<Contest> filter(String s) {
        if (s.equals("")) return contestList;
        ArrayList<Contest> tempList = new ArrayList<Contest>();
        if(contestList != null) {
            int length = contestList.size();
            int i=0;
            while(i < length){
                Contest item = contestList.get(i);
                if (item.getName().startsWith(s))
                    tempList.add(item);
                i++;
            }
        }
        return tempList;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.contest_search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search for contest name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                contestAdapter.setContestArrayList(filter(s));
                contestAdapter.notifyDataSetChanged();
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}