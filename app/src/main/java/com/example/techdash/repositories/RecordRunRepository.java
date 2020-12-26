package com.example.techdash.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.techdash.models.History;
import com.example.techdash.models.Route;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RecordRunRepository {
    private static final String TAG = RecordRunRepository.class.getSimpleName();
    private static RecordRunRepository INSTANCE;
    private MediatorLiveData<Route> route;
    private MediatorLiveData<Double> distance;
    private MediatorLiveData<Double> pace;
    private FirebaseFirestore db;

    RecordRunRepository() {
        route = new MediatorLiveData<>();
        distance = new MediatorLiveData<>();
        pace = new MediatorLiveData<>();
        db = FirebaseFirestore.getInstance();
    }

    public void addDataSource(LiveData<Route> route) {
        this.route.addSource(route, this.route::setValue);
        this.distance.addSource(route, new Observer<Route>() {
            @Override
            public void onChanged(Route route) {
                distance.setValue(route.calculateDistance());
            }
        });
        this.pace.addSource(route, new Observer<Route>() {
            @Override
            public void onChanged(Route route) {
                pace.setValue(route.calculatePace());
            }
        });
    }

    public void resetVariables() {
        route.setValue(new Route());
        distance.setValue((double) 0);
        pace.setValue((double) 0);
    }

    public void removeDataSource(LiveData<Route> route) {
        this.route.removeSource(route);
        this.distance.removeSource(route);
        this.pace.removeSource(route);
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

    public void save(String uid, Route route) {
        String dateTime = Calendar.getInstance().getTime().toString();

        String encoded = route.encodeRoute();
        long totalTime = route.getTotalTime();
        double pace = route.calculatePace();
        double distance = route.calculateDistance();
        Map<String, Object> map = new HashMap<>();
        map.put("distance", distance);
        map.put("route", encoded);
        map.put("total_time", totalTime);
        map.put("pace", pace);
        map.put("date", dateTime);

        db.collection("users").document(uid)
                .collection("records").document(dateTime)
                .set(map)
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
    }

    public ArrayList<History> fetch(String uid) {
        ArrayList<History> histories = new ArrayList<>();
        db.collection("users").document(uid)
                .collection("records").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Fetched data successfully " + task.getResult().size());
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Map<String, Object> data = doc.getData();
                                histories.add(new History(data));
                            }
                        } else {
                            Log.d(TAG, "Failed to fetch data");
                        }
                    }
                });
        Log.d("AAA",String.valueOf(histories.size()));
        return histories;
    }
}
