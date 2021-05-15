package com.example.techdash.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.techdash.models.User;
import com.example.techdash.repositories.UserRepository;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UserViewModel extends ViewModel {
    private static final String TAG = UserViewModel.class.getSimpleName();
    private MediatorLiveData<User> user = new MediatorLiveData<>();

    public UserViewModel() {
        user.addSource(UserRepository.getInstance().getUser(), newUser -> user.setValue(newUser));
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void loginWithFacebook(AccessToken accessToken) {
        UserRepository.getInstance().loginWithFacebook(accessToken);
    }

    public void logout() {
        UserRepository.getInstance().logout();
    }
}
