package com.example.techdash.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class LoginFragment extends Fragment {
    private final static String TAG = LoginFragment.class.getSimpleName();
    private UserViewModel userViewModel;
    private LoginButton fbButton;
    private CallbackManager callbackManager;
    private EditText edtEmail, edtPassword;
    private Button btnSignin, btnSignup;
    private FirebaseAuth mAuth;

    private void Signin(NavController navController) {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sign in", "Sign in success");
                            AuthResult authResult = task.getResult();
                            userViewModel.loginWithAccount(authResult);
                            navController.popBackStack();
                        } else {
                            Log.w("Sign in", task.getException());
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    navController.popBackStack();
                }
            }
        });

        edtEmail = getView().findViewById(R.id.si_email);
        edtPassword = getView().findViewById(R.id.si_password);
        //Dang nhap

        btnSignin = getView().findViewById(R.id.si_signin);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signin(navController);
            }
        });

        //Dang ky
        btnSignup = getView().findViewById(R.id.si_signup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.signupFragment);
            }
        });

        //Dang nhap facebook
        fbButton = getView().findViewById(R.id.loginFacebook);
        fbButton.setFragment(this);
        callbackManager = CallbackManager.Factory.create();
        fbButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Login facebook", "Login success");
                AccessToken accessToken = loginResult.getAccessToken();
                userViewModel.loginWithFacebook(accessToken);
                navController.popBackStack();
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