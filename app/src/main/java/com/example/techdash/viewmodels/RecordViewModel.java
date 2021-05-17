package com.example.techdash.viewmodels;

import android.graphics.Bitmap;
import android.media.DrmInitData;
import android.os.Parcelable;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.example.techdash.models.Route;
import com.example.techdash.repositories.RecordRunRepository;

import javax.annotation.Nullable;

public class RecordViewModel extends ViewModel {
    private static final String TAG = RecordViewModel.class.getSimpleName();

    private MediatorLiveData<Route> mRoute = new MediatorLiveData<>();
    private MediatorLiveData<Double> mDistance = new MediatorLiveData<>();
    private MediatorLiveData<Double> mPace = new MediatorLiveData<>();
    private MediatorLiveData<String> mUid = new MediatorLiveData<>();

    private Route mStoredRoute = new Route();
    private String mStoredUid;
    @Nullable
    private Long mStartTime;

    public RecordViewModel() {
        Log.d(TAG, "RecordViewModel created");
        mRoute.addSource(RecordRunRepository.getInstance().getRoute(), newRoute -> mRoute.setValue(newRoute));
        mDistance.addSource(RecordRunRepository.getInstance().getDistance(), newDistance -> mDistance.setValue(newDistance));
        mPace.addSource(RecordRunRepository.getInstance().getPace(), newPace -> mPace.setValue(newPace));
        mUid.addSource(RecordRunRepository.getInstance().getUid(), newUid -> mUid.setValue(newUid));
    }

    public void saveRoute(Route newRoute) {
        mStoredRoute = newRoute;
    }

    public void saveUid(String newUid) {
        mStoredUid = newUid;
    }

    public LiveData<Route> getRoute() {
        return mRoute;
    }

    public LiveData<Double> getDistance() {
        return mDistance;
    }

    public LiveData<String> getUid() {
        return mUid;
    }

    public LiveData<Double> getPace() {
        return mPace;
    }

    public void save(Bitmap bitmap) {
        RecordRunRepository.getInstance().save(mStoredUid, mStoredRoute, bitmap);
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
