package com.example.techdash.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.techdash.R;

public class FriendFragment extends Fragment {
    private static final String TAG = FriendFragment.class.getSimpleName();
    private EditText searchBar;
    private ImageButton searchButton;

    public FriendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_friend, container, false);
        searchBar= v.findViewById(R.id.searchBar);
        searchButton = v.findViewById(R.id.searchButton);



        return v;
    }
}