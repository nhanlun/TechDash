package com.example.techdash.viewmodels;

import android.os.Parcelable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.techdash.models.Route;
import com.example.techdash.repositories.RecordRunRepository;

import javax.annotation.Nullable;

public class RecordViewModel extends ViewModel {
    private static final String TAG = RecordViewModel.class.getSimpleName();

    private String uid;
    private Route mRoute;
    @Nullable
    private Long mStartTime;

    public RecordViewModel() {
        mRoute = new Route();
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
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

    public void save(Route route) {
        if (uid == null) uid = "X5P3gKcD6CUETVxiXIDDZ4arNwh2";
        RecordRunRepository.getInstance().save(uid, route);
    }

    public void storeRoute(Route route) {
        mRoute = route;
    }

    public Route getStoredRoute() {
        return mRoute;
    }

    @Nullable
    public Long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(final long startTime) {
        this.mStartTime = startTime;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public void resetVariables() {
        RecordRunRepository.getInstance().resetVariables();
    }
}
