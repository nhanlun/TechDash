package com.example.techdash.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.techdash.R;
import com.example.techdash.models.User;
import com.example.techdash.viewmodels.UserViewModel;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginFragment extends Fragment {
    private final static String TAG = LoginFragment.class.getSimpleName();
    private UserViewModel userViewModel;
    private LoginButton fbButton;
    private CallbackManager callbackManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        fbButton = getView().findViewById(R.id.loginFacebook);
        fbButton.setFragment(this);
        callbackManager = CallbackManager.Factory.create();
        final NavController navController = Navigation.findNavController(view);
        userViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null){
                    navController.navigate(R.id.homeFragment);
                }
            }
        });


        fbButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Login facebook", "Login success");
                AccessToken accessToken = loginResult.getAccessToken();
                userViewModel.createNewUserFromToken(accessToken);
                Navigation.findNavController(getView()).popBackStack();
            }

            @Override
            public void onCancel() {
                Log.d("Login facebook", "Login cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Login facebook", "Login error");
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}