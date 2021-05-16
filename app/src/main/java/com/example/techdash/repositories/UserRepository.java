package com.example.techdash.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.techdash.models.User;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private static final String TAG = UserRepository.class.getSimpleName();
    private static UserRepository INSTANCE = null;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private MutableLiveData<User> user = new MutableLiveData<>(null);

    private UserRepository() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Log.d(TAG, "Logged in");
            fetchUser(currentUser);
        }
    }

    public static UserRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserRepository();
        }
        return INSTANCE;
    }

    public LiveData<User> getUser() {
        return user;
    }

    void fetchUser(FirebaseUser firebaseUser) {
        String uid = firebaseUser.getUid();
        db.collection("users").document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> map = task.getResult().getData();
                            if (map != null) {
                                Log.d(TAG, "Fetched user");
                                user.postValue(new User(map));
                            } else {
                                Log.d(TAG, "User not found, creating new user");
                                createUser(firebaseUser);
                            }
                        } else {
                        }
                    }
                });
    }

    void createUser(FirebaseUser firebaseUser) {
        String uid = firebaseUser.getUid();
        String name = firebaseUser.getDisplayName();
        User newUser = new User(uid, name, 0);
        HashMap<String, Object> map = newUser.toHashMap();
        db.collection("users").document(uid)
                .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Created a new user");
                    user.postValue(newUser);
                }
            }
        });
    }

    public void loginWithFacebook(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Login successfully");
                    FirebaseUser currentUser = auth.getCurrentUser();
                    fetchUser(currentUser);
                } else {
                    Log.d(TAG, "Login failed");
                }
            }
        });
    }

    public void loginWithAccount(AuthResult authResult) {
        FirebaseUser currentUser = auth.getCurrentUser();
        fetchUser(currentUser);
    }

    public void logout() {
        auth.signOut();
        LoginManager.getInstance().logOut();
        user.setValue(null);
    }
}
