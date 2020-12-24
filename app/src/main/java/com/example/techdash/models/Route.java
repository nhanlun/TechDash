package com.example.techdash.models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class Route implements Serializable {
    private ArrayList<RoutePoint> route;

    public Route() {
        route = new ArrayList<>();
    }

    public void add(RoutePoint point) {
        route.add(point);
    }

    public ArrayList<LatLng> getListLatLng() {
        ArrayList<LatLng> res = new ArrayList<>();
        for (RoutePoint i : route) {
            res.add(i.getLatLng());
        }
        return res;
    }
}
