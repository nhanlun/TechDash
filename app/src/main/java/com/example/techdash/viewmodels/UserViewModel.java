package com.example.techdash.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UserViewModel extends ViewModel {
    private static final String TAG = UserViewModel.class.getSimpleName();
    private MutableLiveData<User> user ;
    private FirebaseAuth mAuth;

    private FirebaseFirestore db;
    public UserViewModel() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = new MutableLiveData<>(null);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            Log.e(TAG, "OK");
        }
        else{
            initiateUser(currentUser);
        }
    }

    private void debug(){
        db.collection("users").document("yolo").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.e("Firestore", task.getResult().get("name").toString());
            }
        });
    }

    public void fetchData(String uid) {
        Log.e(TAG, "Fetching");
        db.collection("users").whereEqualTo("uid", uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    // TODO: init user here
                    Log.d(TAG, doc.getId() + ": " + doc.getData());
                }
            }
        });
    }

    public void initiateUser(FirebaseUser FBuser){
        String uid = FBuser.getUid();
        String name = FBuser.getDisplayName();
        // TODO; Create user from uid
        DocumentReference documentReference = db.collection("user").document(uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()){
                        //TODO: Fetch data
                        Map<String, Object> map = snapshot.getData();
                        if (map != null) {
                            String name = map.get("name").toString();
                            String uid = map.get("uid").toString();
                            int energy = Integer.parseInt(map.get("energy").toString());
                            Log.e(TAG, name + " " + uid);
                            User loadUser = new User(uid, name, energy);
                            user.postValue(loadUser);
                        }
                        else{
                            Log.e(TAG, "Parsing document snapshot error");
                        }
                    }
                    else {
                        createNewProfile(FBuser);
                    }
                }
                else{
                    Log.e(TAG, "Failed with " + task.getException());
                }
            }
        });
    }

    private void createNewProfile(FirebaseUser FBuser) {
        String uid = FBuser.getUid();
        String name = FBuser.getDisplayName();
        int energy = 0;
        User newUser = new User(uid, name, energy);
        user.postValue(newUser);
        createProfileDocument(FBuser);
    }

    public void createNewUserFromToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    initiateUser(user);
                    Log.d(TAG, "User sign in successfully with Uid: " + user.getUid());
                }
            }
        });
    }

    public LiveData<User> getUser() {
        return user;
    }

    private String getCurrentUid(){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null)
            return null;
        else
            return user.getUid();
    }

    public void logout() {
        /*
         * Comment line LoginManager.getInstance().logOut();
         * to create a logout bug (Only use for testing
         * The bug is after logout Firebase account, user does not logout facebook account.
         ********************************/
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        user = new MutableLiveData<>(null);
    }

    private void createProfileDocument(FirebaseUser user){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = user.getUid();
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("name", user.getDisplayName());
        map.put("energy", 0);
        db.collection("users").document(uid).set(map);
    }
}
