package com.example.techdash.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.techdash.R;
import com.example.techdash.adapters.ContestAdapter;
import com.example.techdash.models.Contest;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ContestFragment extends Fragment {
    RecyclerView recyclerView;
    private static final String TAG = ContestFragment.class.getSimpleName();
    View v;
    ArrayList<Contest> contestList = new ArrayList<>();
    ContestAdapter contestAdapter;

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

        String date = Calendar.getInstance().getTime().toString();

        contestList.add(new Contest("123", "Uni of Science", "HCMUS Marathon", "Admin",
                date, date, new ArrayList<String>(0)));
        contestList.add(new Contest("456", "Ho Con Rua", "Rua Marathon", "Admin",
                date, date, new ArrayList<String>(0)));
        contestList.add(new Contest("789", "Bitexco Tower", "Bitexco Marathon", "Admin",
                date, date, new ArrayList<String>(0)));
        contestList.add(new Contest("111", "Cho Ben Thanh", "Ben Thanh Marathon", "Admin",
                date, date, new ArrayList<String>(0)));
        contestList.add(new Contest("222", "Pho di bo Nguyen Hue", "Chay dua o Pho di bo", "Admin",
                date, date, new ArrayList<String>(0)));
        contestList.add(new Contest("333", "Song Sai Gon", "Chay dua tren mat song SG", "Admin",
                date, date, new ArrayList<String>(0)));

        recyclerView = v.findViewById(R.id.rvContest);
        contestAdapter = new ContestAdapter(contestList, getContext(), R.layout.fragment_contest_row);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(contestAdapter);

        /*RecordRunRepository.getInstance().fetch(uid).observe(getViewLifecycleOwner(), new Observer<ArrayList<Contest>>() {
            @Override
            public void onChanged(ArrayList<Contest> contests) {
                contestAdapter.setContestArrayList(contests);
                contestAdapter.notifyDataSetChanged();
            }
        });*/
        return v;
    }

    ArrayList<Contest> filter(String s) {
        if (s.equals("")) return contestList;
        ArrayList<Contest> tempList = new ArrayList<Contest>();
        //constraint is the result from text you want to filter against.
        //objects is your data set you will filter from
        if(contestList != null) {
            int length = contestList.size();
            int i=0;
            while(i < length){
                Contest item = contestList.get(i);
                //do whatever you wanna do here
                //adding result set output array
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