package com.example.techdash.repositories;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.techdash.models.Route;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RecordRunRepository {
    private static final String TAG = RecordRunRepository.class.getSimpleName();
    private static RecordRunRepository INSTANCE;
    private MediatorLiveData<Route> route;
    private MediatorLiveData<Double> distance;
    private FirebaseFirestore db;

    RecordRunRepository() {
        route = new MediatorLiveData<>();
        distance = new MediatorLiveData<>();
        db = FirebaseFirestore.getInstance();
    }

    public void addDataSource(LiveData<Route> route) {
        this.route.addSource(route, this.route::setValue);
        this.distance.addSource(route, new Observer<Route>() {
            @Override
            public void onChanged(Route route) {
                distance.setValue(calculateDistance(route.getListLatLng()));
            }
        });
    }

    public double calculateDistance(ArrayList<LatLng> arrayLatLng) {
        double dis = 0;
        float[] res = new float[10];
        LatLng cur = arrayLatLng.get(0);
        for (int i = 1; i < (int)arrayLatLng.size() - 1; ++i) {
            LatLng next = arrayLatLng.get(i);
            Location.distanceBetween(cur.latitude, cur.longitude, next.latitude, next.longitude, res);
            if (res[0] > 1.2)
                dis += res[0];
            cur = next;
        }
        return dis / 1000;
    }

    public void removeDataSource(LiveData<Route> route) {
        this.route.removeSource(route);
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

    public void save(String uid, String encoded) {
        // TODO: save string
        String dateTime = Calendar.getInstance().getTime().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("route", encoded);

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
}
