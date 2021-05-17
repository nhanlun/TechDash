package com.example.techdash.repositories;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.techdash.broadcasts.RecordBroadcast;
import com.example.techdash.models.History;
import com.example.techdash.models.Route;
import com.example.techdash.services.RecordService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RecordRunRepository {
    private static final String TAG = RecordRunRepository.class.getSimpleName();
    private static RecordRunRepository INSTANCE;
    private MediatorLiveData<Route> route = new MediatorLiveData<>();
    private MediatorLiveData<Double> distance = new MediatorLiveData<>();
    private MediatorLiveData<Double> pace = new MediatorLiveData<>();
    private MediatorLiveData<String> uid = new MediatorLiveData<>();

    private FirebaseFirestore db;
    private FirebaseStorage storage;

    RecordRunRepository() {
        Log.d(TAG, "Record Run Repository created");
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public void addDataSource(RecordBroadcast broadcast) {
        this.route.addSource(broadcast.getRoute(), newRoute -> {
            this.route.setValue(newRoute);
        });
        this.distance.addSource(broadcast.getRoute(), new Observer<Route>() {
            @Override
            public void onChanged(Route route) {
                distance.setValue(route.calculateDistance());
            }
        });
        this.pace.addSource(broadcast.getRoute(), new Observer<Route>() {
            @Override
            public void onChanged(Route route) {
                pace.setValue(route.calculatePace());
            }
        });
        this.uid.addSource(broadcast.getUid(), newUid -> {
            this.uid.setValue(newUid);
        });
    }

    public void removeDataSource(RecordBroadcast broadcast) {
        this.route.removeSource(broadcast.getRoute());
        this.distance.removeSource(broadcast.getRoute());
        this.pace.removeSource(broadcast.getRoute());
        this.uid.removeSource(broadcast.getUid());
    }

    public static RecordRunRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RecordRunRepository();
        }
        return INSTANCE;
    }

    public LiveData<Route> getRoute() {
        return route;
    }

    public LiveData<Double> getDistance() {
        return distance;
    }

    public LiveData<Double> getPace() {
        return pace;
    }

    public LiveData<String> getUid() {
        return uid;
    }

    public void save(String uid, Route route, Bitmap bitmap) {
        if (uid == null || route == null) {
            Log.d(TAG, "Oh no wtf " + uid);
            return;
        }

        Map<String, Object> map = route.toHashMap();
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        map.put("time_in_millis", timeInMillis);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        db.collection("users").document(uid)
                .collection("records")
                .document(String.valueOf(timeInMillis)).set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Saved record successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Failed to save record");
                    }
                });

        storage.getReference().child("images/" + timeInMillis)
                .putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "Upload image successful");
                    }
                });
    }

    public LiveData<ArrayList<History>> fetch(String uid) {
        MutableLiveData<ArrayList<History>> histories = new MutableLiveData<>();
        db.collection("users").document(uid)
                .collection("records")
                .orderBy("time_in_millis", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Fetched data successfully " + task.getResult().size());
                            ArrayList<History> newHistory = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Map<String, Object> data = doc.getData();
                                newHistory.add(new History(data));
                            }
                            histories.postValue(newHistory);
                        } else {
                            Log.d(TAG, "Failed to fetch data");
                        }
                    }
                });
        return histories;
    }
}
