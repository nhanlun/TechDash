package com.example.techdash.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.techdash.R;
import com.example.techdash.models.User;
import com.example.techdash.viewmodels.UserViewModel;

public class HomeFragment extends Fragment {
    final static String TAG = HomeFragment.class.getSimpleName();
    private UserViewModel userViewModel;

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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        final NavController navController = Navigation.findNavController(view);
        Log.e(TAG, "Created bro");
        setupConditionalNavigation(navController);
        setupButton(navController);
        userViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.d("null", "user changed");
                if(user != null){
                    displayUserProfile(user);
                    Log.d("null", "user is not null");
                }
                else
                    Log.d("null","nn");
            }
        });
    }

    private void setupConditionalNavigation(NavController navController) {
        userViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user == null) {
                    navController.navigate(R.id.loginFragment);
                }
            }
        });
    }

    private void setupButton(NavController navController) {
        Button button = getView().findViewById(R.id.logout_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Logging out");
                userViewModel.logout();
            }
        });
    }

    private void displayUserProfile(User user){
        TextView tvUserName = getView().findViewById(R.id.tvUserName);
        tvUserName.setText(user.getName());
        TextView tvUserEnergy = getView().findViewById(R.id.tvEnergy);
        tvUserEnergy.setText(String.valueOf(user.getEnergy()));
    }
}