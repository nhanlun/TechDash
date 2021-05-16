package com.example.techdash.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.techdash.R;
import com.example.techdash.models.Route;
import com.example.techdash.viewmodels.RecordViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.button.MaterialButton;
import com.google.maps.android.PolyUtil;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class FinishFragment extends Fragment {
    private static final String TAG = FinishFragment.class.getSimpleName();
    private RecordViewModel recordViewModel;
    private MaterialButton finishButton;
    private TextView totalDistance;
    private TextView textViewPace;
    private TextView textViewTotalTime;
    private MapView mapView;
    private GoogleMap map;
    private Polyline polyline;

    public FinishFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_finish, container, false);

        recordViewModel = new ViewModelProvider(requireActivity()).get(RecordViewModel.class);
        mapView = v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                PolylineOptions option = new PolylineOptions();
                option.addAll(recordViewModel.getRoute().getValue().getListLatLng());
                polyline = map.addPolyline(option);
                moveToCenter(polyline);
            }
        });

        finishButton = v.findViewById(R.id.finishButon);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                 TODO: save record
                Route route = recordViewModel.getRoute().getValue();
                if (route == null)
                    Log.d(TAG, "Why is the route null when pressing finish");
                recordViewModel.save();
                requireActivity().finish();
            }
        });

        totalDistance = v.findViewById(R.id.tvTotalDistance);
        recordViewModel.getDistance().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double distance) {
                totalDistance.setText(String.format(Locale.getDefault(), "%.2f km", distance));
            }
        });

        textViewPace = v.findViewById(R.id.tvPace);
        recordViewModel.getPace().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double pace) {
                textViewPace.setText(String.format(Locale.getDefault(), "%.1f /km", pace));
            }
        });

        textViewTotalTime = v.findViewById(R.id.tvTime);
        long totalTime = recordViewModel.getTotalTime();
        String data = String.format(Locale.getDefault(), "%02d:%02d:%02d",
                TimeUnit.SECONDS.toHours(totalTime),
                TimeUnit.SECONDS.toMinutes(totalTime) % 60,
                TimeUnit.SECONDS.toSeconds(totalTime) % 60
                );

        textViewTotalTime.setText(data);
        return v;
    }

    private void moveToCenter(Polyline polyline) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng i : polyline.getPoints()) {
            builder.include(i);
        }

        LatLngBounds bounds = builder.build();
        int padding = 100;

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        map.animateCamera(cameraUpdate);
    }

    @Override
    public void onStart() {
        mapView.onStart();
        super.onStart();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        mapView.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        mapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
}