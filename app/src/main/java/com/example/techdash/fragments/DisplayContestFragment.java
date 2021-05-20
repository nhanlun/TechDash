package com.example.techdash.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techdash.R;
import com.example.techdash.adapters.ContestAdapter;
import com.example.techdash.adapters.ParticipantAdapter;
import com.example.techdash.models.Contest;
import com.example.techdash.models.User;
import com.example.techdash.viewmodels.ContestViewModel;
import com.example.techdash.viewmodels.UserViewModel;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DisplayContestFragment extends Fragment {
    private Button btn;
    private UserViewModel userViewModel;
    private ContestViewModel contestViewModel;
    private TextView starttime, endtime, creator, destination, name;
    private RecyclerView partiRecyclerView;
    private ParticipantAdapter participantAdapter;
    private ArrayList<String> partiList = new ArrayList<>();;
    private User currentUser;

    public DisplayContestFragment() {
         //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_contest, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Contest contest = DisplayContestFragmentArgs.fromBundle(getArguments()).getContest();
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        currentUser = userViewModel.getUser().getValue();

        name = getView().findViewById(R.id.ct_name);
        creator = getView().findViewById(R.id.ct_creator);
        destination = getView().findViewById(R.id.ct_destination);
        name.setText(contest.getName());
        destination.setText(contest.getDestination());
        creator.setText(String.format("Created by %s", contest.getCreator()));

        starttime = getView().findViewById(R.id.ct_starttime);
        endtime = getView().findViewById(R.id.ct_endtime);
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sf = new SimpleDateFormat("EEE, dd-MM-yyyy, HH:mm", Locale.ENGLISH);

        long start_InMili = Long.parseLong(contest.getStartTime());
        long end_InMili = Long.parseLong(contest.getEndTime());
        calendar.setTimeInMillis(start_InMili);
        starttime.setText(sf.format(calendar.getTime()));
        calendar.setTimeInMillis(end_InMili);
        endtime.setText(sf.format(calendar.getTime()));

        btn = view.findViewById(R.id.ct_btn);
        partiRecyclerView = view.findViewById(R.id.ct_participants);
        participantAdapter = new ParticipantAdapter(partiList, getContext(), R.layout.fragment_display_contest_row);

        partiRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        partiRecyclerView.setLayoutManager(linearLayoutManager);
        partiRecyclerView.setAdapter(participantAdapter);

        contestViewModel = new ViewModelProvider(requireActivity()).get(ContestViewModel.class);
        contestViewModel.fetchParticipants(contest.getID()).observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> participants) {
                contest.setParticipants(participants);
                participantAdapter.setParticipantIDList(participants);
                participantAdapter.notifyDataSetChanged();

                if (contest.isEnded()) btn.setVisibility(View.INVISIBLE);
                else if (contest.isParticipate(currentUser.getUid()) && contest.getCreator().equals(currentUser.getName())) {
                    btn.setText("Edit");
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                } else {
                    if (!contest.isParticipate(currentUser.getUid())) btn.setText("Join");
                    else btn.setText("Unjoin");
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (btn.getText().equals("Join")) {
                                if (!contest.isParticipate(currentUser.getUid())) {
                                    contestViewModel.addParticipant(contest.getID(), currentUser.getUid());

                                    btn.setText("Unjoin");
                                }
                            } else {
                                if (contest.isParticipate(currentUser.getUid())) {
                                    contestViewModel.removeParticipant(contest.getID(), currentUser.getUid());
                                    btn.setText("Join");
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}
