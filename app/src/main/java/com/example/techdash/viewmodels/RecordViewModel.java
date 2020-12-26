package com.example.techdash.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.techdash.models.Route;
import com.example.techdash.repositories.RecordRunRepository;

public class RecordViewModel extends ViewModel {
    private static final String TAG = RecordViewModel.class.getSimpleName();

    private Route mRoute;
    public RecordViewModel() {
        mRoute = new Route();
    }

    public LiveData<Route> getRoute() {
        return RecordRunRepository.getInstance().getRoute();
    }

    public LiveData<Double> getDistance() {
        return RecordRunRepository.getInstance().getDistance();
    }

    public LiveData<Double> getPace() {
        return RecordRunRepository.getInstance().getPace();
    }

    public void save(String uid, Route route) {
        RecordRunRepository.getInstance().save(uid, route);
    }

    public void storeRoute(Route route) {
        mRoute = route;
    }

    public Route getStoredRoute() {
        return mRoute;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
