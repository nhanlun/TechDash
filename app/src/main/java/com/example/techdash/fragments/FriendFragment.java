package com.example.techdash.fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techdash.R;
import com.example.techdash.adapters.FriendAdapter;
import com.example.techdash.adapters.FriendListAdapter;
import com.example.techdash.models.User;
import com.example.techdash.repositories.UserRepository;
import com.example.techdash.viewmodels.FriendViewModel;

import java.util.ArrayList;

public class FriendFragment extends Fragment {
    private static final String TAG = FriendFragment.class.getSimpleName();
    private FriendViewModel friendViewModel;
    private EditText searchBar;
    private ImageButton searchButton;
    private RecyclerView friendList;
    private FriendListAdapter friendListAdapter;

    public FriendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        friendViewModel = new ViewModelProvider(this).get(FriendViewModel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_friend, container, false);

        searchBar = v.findViewById(R.id.searchBar);
        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    //change to fragment search friend
                }
            }
        });
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                   // search();
                    return true;
                }
                return false;
            }
        });
        searchButton = v.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chuyen fragment search friend
            }
        });

        friendList = v.findViewById(R.id.friendList);
        LinearLayoutManager linearLayoutManagerForFriendList = new LinearLayoutManager(getContext());
        friendList.setLayoutManager(linearLayoutManagerForFriendList);
        ArrayList<User> arrayListForFriendList = new ArrayList<User>();
        friendListAdapter = new FriendListAdapter(arrayListForFriendList, friendViewModel);
        friendList.setAdapter(friendListAdapter);
        friendViewModel.getFriendList().observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                friendListAdapter.setFriendArrayList(users);
                friendListAdapter.notifyDataSetChanged();
            }
        });

        return v;
    }

}