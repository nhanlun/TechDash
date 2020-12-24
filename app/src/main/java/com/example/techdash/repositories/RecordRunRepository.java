package com.example.techdash.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.techdash.models.Route;

public class RecordRunRepository {
    private static RecordRunRepository INSTANCE;
    private MediatorLiveData<Route> route;

    RecordRunRepository() {
        route = new MediatorLiveData<>();
    }

    public void addDataSource(LiveData<Route> route) {
        this.route.addSource(route, this.route::setValue);
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
}
