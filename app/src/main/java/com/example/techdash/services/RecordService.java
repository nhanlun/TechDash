package com.example.techdash.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.techdash.R;
import com.example.techdash.models.Route;
import com.example.techdash.models.RoutePoint;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;

public class RecordService extends Service {
    private static final String TAG = RecordService.class.getSimpleName();
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Notification notification;
    private Route route;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        route = new Route();
        locationRequest = LocationRequest.create();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.d(TAG, "Location: " + locationResult.getLastLocation().getLatitude() + " " + locationResult.getLastLocation().getLongitude());
                double lat = locationResult.getLastLocation().getLatitude();
                double lng = locationResult.getLastLocation().getLongitude();
                long time = Calendar.getInstance().getTimeInMillis();
                route.add(new RoutePoint(lat, lng, time));

                Intent intent = new Intent();
                intent.setAction(getString(R.string.intent_action));
                intent.putExtra("route", route);
                intent.setPackage("com.example.techdash");
                sendBroadcast(intent);
            }
        };

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager service = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("techdash_service", "back ground service", NotificationManager.IMPORTANCE_DEFAULT);
            service.createNotificationChannel(channel);
            notification = new Notification.Builder(this, "techdash_service")
                    .setContentTitle(getString(R.string.techdash_is_working))
                    .setContentText(getString(R.string.your_location_is_being_recorded))
                    .build();
            Log.d(TAG, "Created notification channel");
        } else {
            notification = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.techdash_is_working))
                    .setContentText(getString(R.string.your_location_is_being_recorded))
                    .build();
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
                .setInterval(3000).setMaxWaitTime(0);
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
