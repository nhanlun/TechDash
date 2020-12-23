package com.example.techdash.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.techdash.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class RecordService extends Service {
    private static final String TAG = RecordService.class.getSimpleName();
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Notification notification;
    private Binder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        RecordService getService() {
            return RecordService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        locationRequest = LocationRequest.create();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                // TODO: do somthing here, put a timer in here
                Log.d(TAG, "Location received " + locationResult.getLastLocation().getLatitude() + " " + locationResult.getLastLocation().getLongitude());
            }
        };
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        NotificationManager service = (NotificationManager) getSystemService(getBaseContext().NOTIFICATION_SERVICE);
        service.createNotificationChannel(new NotificationChannel("my_service", "back ground service", NotificationManager.IMPORTANCE_DEFAULT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, "my_service")
                    .setContentTitle("TechDash is working")
                    .setContentText("Your location is being recorded for activity tracking")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();
        }
        else {
            // TODO: Do something here
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Start tracking");
        requestLocationUpdate();
        startForeground(12345, notification);
        return super.onStartCommand(intent, flags, startId);
    }

    public void requestLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setFastestInterval(1000)
                .setInterval(5000).setMaxWaitTime(0);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Stopped tracking");
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        stopForeground(true);
        super.onDestroy();
    }
}
