package com.example.techdash.viewmodels;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.techdash.services.RecordService;

import java.lang.ref.WeakReference;

public class RecordViewModel extends ViewModel {
    private static final String TAG = RecordViewModel.class.getSimpleName();
    private WeakReference<Context> context;
    private ServiceConnection connection;
    private Intent intent;
    private boolean bound;

    public RecordViewModel() {
    }

    public void setContext(Context context) {
        this.context = new WeakReference<>(context);
    }

    public ServiceConnection getConnection() {
        return connection;
    }

    public void startRecording() {
        if (context != null) {
            intent = new Intent(context.get(), RecordService.class);
            Log.d(TAG, "Start recording");
            connection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {

                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
            context.get().startService(intent);
            bound = context.get().bindService(intent, connection, Context.BIND_WAIVE_PRIORITY);
        }
    }

    public void stopRecording() {
        unbind();
        context.get().stopService(intent);
    }

    public void unbind() {
        if (context != null) {
            if (bound) {
                context.get().unbindService(connection);
                bound = false;
            }
        }
    }

    @Override
    protected void onCleared() {
        unbind();
        super.onCleared();
    }
}
