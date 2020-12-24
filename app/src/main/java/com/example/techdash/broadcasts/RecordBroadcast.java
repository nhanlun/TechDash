package com.example.techdash.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.techdash.models.Route;

public class RecordBroadcast extends BroadcastReceiver {
    private static final String TAG = RecordBroadcast.class.getSimpleName();
    private MutableLiveData<Route> route;

    public RecordBroadcast() {
        route = new MutableLiveData<>();
    }

    public LiveData<Route> getRoute() {
        return route;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received broadcast");
        route.setValue((Route) intent.getSerializableExtra("route"));
    }
}
