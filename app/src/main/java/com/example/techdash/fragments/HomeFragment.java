package com.example.techdash.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.techdash.R;
import com.example.techdash.activities.RecordRunActivity;
import com.example.techdash.viewmodels.UserViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO: remove this test code
        Intent intent = new Intent(getActivity(), RecordRunActivity.class);
        startActivityForResult(intent, 123);

        // TODO: remove this test code
//        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
//        userViewModel.fetchData("IoGKF8dtNZWMOnAwWRsWnVgvVoI3");

        // TODO: get UserViewModel and observe

        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}