package com.example.techdash.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.techdash.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class UserViewModel extends ViewModel {
    private static final String TAG = UserViewModel.class.getSimpleName();
    private MutableLiveData<User> user;

    private FirebaseFirestore db;
    public UserViewModel() {
        db = FirebaseFirestore.getInstance();
        user = new MutableLiveData<>();
    }

    public void fetchData(String uid) {
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

    public void createNewUserData(String uid) {
        // TODO: initialize data
    }
}
