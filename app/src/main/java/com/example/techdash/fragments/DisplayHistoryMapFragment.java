package com.example.techdash.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.techdash.R;
import com.example.techdash.models.History;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class DisplayHistoryMapFragment extends Fragment {
    private MapView mapView;
    private GoogleMap map;
    private Button shareButton;
    private Polyline polyline;
    private List<LatLng> route;
    private Bitmap bitmap;


    public DisplayHistoryMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_history_map, container, false);
        shareButton = view.findViewById(R.id.share_button);
        shareButton.setOnClickListener(v -> {
            // This code snippet is used to capture and share photo via facebook
            Log.e("CLICK", "CLICKED");
            captureImage();
        });
        return view;
    }

    private void captureImage() {
        moveToCenter(polyline);
        GoogleMap.SnapshotReadyCallback callback = snapshot -> {
            Log.e("CAPTURE", "CAPTURE COMPLETE");
            bitmap = snapshot;
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .setCaption("Yo đua không bạn ei??")
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
            ShareDialog dialog = new ShareDialog(getActivity());
            dialog.show(content);
        };
        map.snapshot(callback);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        History history = DisplayHistoryMapFragmentArgs.fromBundle(getArguments()).getHistory();
        route = history.getLatLngs();
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(googleMap -> {
            map = googleMap;
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.addAll(route);
            polyline = map.addPolyline(polylineOptions);
            moveToCenter(polyline);
        });
    }

    private void moveToCenter(Polyline polyline) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i = 0; i < polyline.getPoints().size();i++){
            builder.include(polyline.getPoints().get(i));
        }

        LatLngBounds bounds = builder.build();
        int padding = 30; // offset from edges of the map in pixels

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        map.animateCamera(cu);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}