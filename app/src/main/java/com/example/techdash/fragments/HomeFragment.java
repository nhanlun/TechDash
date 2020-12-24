package com.example.techdash.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
        // TODO: remove this test code
//        Intent intent = new Intent(requireActivity(), RecordRunActivity.class);
//        startActivity(intent);

        // TODO: remove this test code
//        Intent intent = new Intent(getActivity(), LoginActivity.class);
//        startActivityForResult(intent, 123);

        // TODO: remove this test code
//        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
//        userViewModel.fetchData("IoGKF8dtNZWMOnAwWRsWnVgvVoI3");

        // TODO: get UserViewModel and observe

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
    }

    private void setupConditionalNavigation(NavController navController) {
        userViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user == null) {
                    Log.e(TAG, "Cai d gi the nay");
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
                navController.navigate(R.id.loginFragment);
            }
        });
    }
}