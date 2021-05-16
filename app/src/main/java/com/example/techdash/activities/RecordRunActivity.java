package com.example.techdash.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.techdash.R;
import com.example.techdash.broadcasts.RecordBroadcast;
import com.example.techdash.fragments.CustomFragmentNavigator;
import com.example.techdash.models.Route;
import com.example.techdash.repositories.RecordRunRepository;
import com.example.techdash.services.RecordService;
import com.example.techdash.viewmodels.RecordViewModel;

public class RecordRunActivity extends AppCompatActivity {

    private static final String TAG = RecordRunActivity.class.getSimpleName();

    private Intent intent;
    private IntentFilter intentFilter;
    private RecordBroadcast broadcast;
    private RecordViewModel recordViewModel;
    private NavController navController;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_run);

        intent = getIntent();
        uid = intent.getStringExtra("uid");

        intent = new Intent(this, RecordService.class);
        intent.putExtra("uid", uid);
        intentFilter = new IntentFilter();
        intentFilter.addAction(getString(R.string.intent_action));
        broadcast = new RecordBroadcast();
        recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);

        recordViewModel.getRoute().observe(this, new Observer<Route>() {
            @Override
            public void onChanged(Route route) {
                recordViewModel.saveRoute(route);
            }
        });
        recordViewModel.getUid().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                recordViewModel.saveUid(s);
            }
        });

        navController = Navigation.findNavController(this, R.id.fragment);
    }

    @Override
    protected void onStart() {
        startService(intent);
        RecordRunRepository.getInstance().addDataSource(broadcast);
        registerReceiver(broadcast, intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(broadcast);
        RecordRunRepository.getInstance().removeDataSource(broadcast);
        super.onStop();
    }

    public void stopRecordActivity(View view) {
        RecordRunRepository.getInstance().removeDataSource(broadcast);
        stopService(intent);
        navController.navigate(R.id.finishFragment);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}