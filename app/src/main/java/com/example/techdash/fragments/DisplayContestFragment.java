//package com.example.techdash.fragments;
//
//import android.graphics.Bitmap;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.example.techdash.R;
//import com.example.techdash.models.Contest;
//import com.example.techdash.models.User;
//import com.example.techdash.viewmodels.ContestViewModel;
//import com.example.techdash.viewmodels.UserViewModel;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.List;
//import java.util.Locale;
//
//public class DisplayContestFragment extends Fragment {
//    private Button shareButton;
//    private Bitmap bitmap = null;
//
//    Button btn;
//    UserViewModel userViewModel;
//    ContestViewModel contestViewModel;
//    TextView starttime, endtime, creator, destination, name;
//    ListView participants;
//
//    public DisplayContestFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_display_contest, container, false);
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        Contest contest = DisplayContestFragmentArgs.fromBundle(getArguments()).get();
//
//        name = getView().findViewById(R.id.ct_name);
//        creator = getView().findViewById(R.id.ct_creator);
//        destination = getView().findViewById(R.id.ct_destination);
//        name.setText(contest.getName());
//        destination.setText(contest.getDestination());
//        creator.setText(String.format("Created by %s", contest.getCreator()));
//
//        starttime = getView().findViewById(R.id.ct_starttime);
//        endtime = getView().findViewById(R.id.ct_endtime);
//        Calendar calendar = Calendar.getInstance();
//
//        SimpleDateFormat sf = new SimpleDateFormat("EEE, dd-MM-yyyy, HH:mm", Locale.ENGLISH);
//
//        long start_InMili = Long.parseLong(contest.getStartTime());
//        long end_InMili = Long.parseLong(contest.getEndTime());
//        calendar.setTimeInMillis(start_InMili);
//        starttime.setText(sf.format(calendar.getTime()));
//        calendar.setTimeInMillis(end_InMili);
//        endtime.setText(sf.format(calendar.getTime()));
//
//        participants = getView().findViewById(R.id.ct_participants);
//        ArrayAdapter<String> participantListAdapter =
//                new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, contest.getParticipants());
//        participants = (ListView) getActivity().findViewById(R.id.ct_participants);
//        participants.setAdapter(participantListAdapter);
//
//        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
//        btn = getView().findViewById(R.id.ct_btn);
//        User currentUser = userViewModel.getUser().getValue();
//        if (contest.checkParticipant(currentUser.getUid()) && contest.getCreator().equals(currentUser.getName())) {
//            btn.setText("Edit");
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
//        } else {
//            if (!contest.checkParticipant(currentUser.getUid())) btn.setText("Join");
//            else btn.setText("Unjoin");
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (btn.getText().equals("Join")) {
//                        if (!contest.checkParticipant(currentUser.getUid())) {
//                            contest.addParticipants(currentUser.getUid());
//                            btn.setText("Unjoin");
//                        }
//                    } else {
//                        if (contest.checkParticipant(currentUser.getUid())) {
//                            contest.removeParticipants(currentUser.getUid());
//                            btn.setText("Join");
//                        }
//                    }
//                    participantListAdapter.notifyDataSetChanged();
//                }
//            });
//        }
//    }
//}