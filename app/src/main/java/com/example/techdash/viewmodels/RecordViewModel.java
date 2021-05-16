package com.example.techdash.viewmodels;

import android.media.DrmInitData;
import android.os.Parcelable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.techdash.models.Route;
import com.example.techdash.repositories.RecordRunRepository;

import javax.annotation.Nullable;

public class RecordViewModel extends ViewModel {
    private static final String TAG = RecordViewModel.class.getSimpleName();

    private String uid;
    private MediatorLiveData<Route> mRoute = new MediatorLiveData<>();
    private MediatorLiveData<Double> mDistance = new MediatorLiveData<>();
    private MediatorLiveData<Double> mPace = new MediatorLiveData<>();
    @Nullable
    private Long mStartTime;

    public RecordViewModel() {
        mRoute.addSource(RecordRunRepository.getInstance().getRoute(), newRoute -> mRoute.setValue(newRoute));
        mDistance.addSource(RecordRunRepository.getInstance().getDistance(), newDistance -> mDistance.setValue(newDistance));
        mPace.addSource(RecordRunRepository.getInstance().getPace(), newPace -> mPace.setValue(newPace));
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public LiveData<Route> getRoute() {
        return mRoute;
    }

    public LiveData<Double> getDistance() {
        return mDistance;
    }

    public LiveData<Double> getPace() {
        return mPace;
    }

    public void save(Route route) {
        RecordRunRepository.getInstance().save(uid, route);
    }

    public long getTotalTime() {
        if (mRoute.getValue() != null)
            return mRoute.getValue().getTotalTime();
        return 0;
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
}
