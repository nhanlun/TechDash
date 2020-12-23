package com.example.techdash.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ComponentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.techdash.R;
import com.example.techdash.services.RecordService;
import com.example.techdash.viewmodels.RecordViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class RecordRunActivity extends AppCompatActivity {

    private static final String TAG = RecordRunActivity.class.getSimpleName();

    private RecordViewModel recordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_run);

        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);
    }

    @Override
    protected void onStart() {
        recordViewModel.setContext(this);
        recordViewModel.startRecording();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}