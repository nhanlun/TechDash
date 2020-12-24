package com.example.techdash.models;

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
}
