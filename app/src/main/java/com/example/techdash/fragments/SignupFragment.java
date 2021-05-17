package com.example.techdash.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.techdash.R;
import com.example.techdash.models.User;
import com.example.techdash.viewmodels.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

public class SignupFragment extends Fragment {
    private final static String TAG = SignupFragment.class.getSimpleName();
    private EditText edtEmail, edtPassword, edtPassword2, edtName;
    private UserViewModel userViewModel;
    private Button btnSignin, btnSignup;
    private FirebaseAuth mAuth;

    private void Signup(NavController navController) {
        mAuth = FirebaseAuth.getInstance();
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String password2 = edtPassword2.getText().toString();
        String name = edtName.getText().toString();
        if (name == null || name.equals("")) {
            Toast.makeText(getActivity(), "please provide your username", Toast.LENGTH_LONG).show();
        } else {
            if (password.equals(password2)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Sign up", "Sign up success");
                                    userViewModel.loginWithAccount(name);
                                    navController.popBackStack();
                                } else {
                                    Log.w("Sign up", task.getException());
                                }
                            }
                        });
            } else {
                Toast.makeText(getActivity(), "re-typed password must be same as password", Toast.LENGTH_LONG).show();
                edtPassword.setText("");
                edtPassword2.setText("");
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);
        edtEmail = getView().findViewById(R.id.su_email);
        edtName = getView().findViewById(R.id.su_name);
        edtPassword = getView().findViewById(R.id.su_password);
        edtPassword2 = getView().findViewById(R.id.su_password2);
        btnSignin = getView().findViewById(R.id.su_signin);
        btnSignup = getView().findViewById(R.id.su_signup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signup(navController);
            }
        });

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }
}