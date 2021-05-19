package com.example.techdash.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.techdash.models.Contest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContestRepository {
    private static final String TAG = ContestRepository.class.getSimpleName();
    private static ContestRepository INSTANCE;
    private FirebaseFirestore db;
    MutableLiveData<ArrayList<Contest>> contests = new MutableLiveData<>();

    private ContestRepository() {
        Log.d(TAG, "created");
        db = FirebaseFirestore.getInstance();
    }

    public static ContestRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ContestRepository();
        }
        return INSTANCE;
    }

    public LiveData<ArrayList<String>> fetchParticipants(String contestId) {
        ArrayList<String> participants = new ArrayList<>();
        MutableLiveData<ArrayList<String>> participantsLiveData = new MutableLiveData<>();
        db.collection("contests").document(contestId).collection("participants").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot q : task.getResult()) {
                                participants.add(q.getId());
                            }
                            Log.d(TAG, "Fetch participants successfully " + participants.size());
                            participantsLiveData.postValue(participants);
                        }
                    }
                });
        return participantsLiveData;
    }

    public LiveData<ArrayList<Contest>> fetch() {
        ArrayList<Contest> tmpContests = new ArrayList<>();
        db.collection("contests").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot q : task.getResult()) {
                                Map<String, Object> map = q.getData();
                                Contest contest = new Contest(map);
                                tmpContests.add(contest);
                            }
                            Log.d(TAG, "Successfully fetched contests " + tmpContests.size());
                            contests.postValue(tmpContests);
                        } else {
                        }
                    }
                });
        return contests;
    }

    public void save(Contest contest) {
        WriteBatch batch = db.batch();
        DocumentReference docRef = db.collection("contests").document(contest.getID());
        batch.set(docRef, contest.toHashMap());
        CollectionReference colRef = db.collection("contests").document(contest.getID()).collection("participants");
        ArrayList<String> participants = contest.getParticipants();
        for (String id : participants) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            batch.set(colRef.document(id), map);
        }
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Successfully added contest");
                } else {
                    Log.d(TAG, "Failed to add the contest");
                }
            }
        });
    }
}
