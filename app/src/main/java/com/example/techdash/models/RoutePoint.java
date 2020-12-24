package com.example.techdash.models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class RoutePoint implements Serializable {
    private final double lat;
    private final double lng;
    private final long time;

    public RoutePoint(double lat, double lng, long time) {
        this.lat = lat;
        this.lng = lng;
        this.time = time;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }

    public long getTime() {
        return time;
    }
}
