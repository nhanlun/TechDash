package com.example.techdash.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.techdash.R;
import com.example.techdash.broadcasts.RecordBroadcast;
import com.example.techdash.repositories.RecordRunRepository;
import com.example.techdash.services.RecordService;

public class RecordRunActivity extends AppCompatActivity {

    private static final String TAG = RecordRunActivity.class.getSimpleName();

    private Intent intent;
    private IntentFilter intentFilter;
    private RecordBroadcast broadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_run);

        intent = new Intent(this, RecordService.class);
        intentFilter = new IntentFilter();
        intentFilter.addAction(getString(R.string.intent_action));
        broadcast = new RecordBroadcast();
    }

    @Override
    protected void onStart() {
        startService(intent);
        RecordRunRepository.getInstance().addDataSource(broadcast.getRoute());
        registerReceiver(broadcast, intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(broadcast);
        RecordRunRepository.getInstance().removeDataSource(broadcast.getRoute());
        super.onStop();
    }
}