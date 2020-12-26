package com.example.techdash.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.List;
import java.util.Map;

public class History {
    private List<LatLng> latLngs;
    private double distance;
    private long totalTime;
    private double pace;
    private String dateTime;

    public History() {

    }

    public History(Map<String, Object> data) {
        try {
            distance = (double) data.get("distance");
            totalTime = (long) data.get("total_time");
            pace = (double) data.get("pace");
            dateTime = (String) data.get("date");
            String encoded = (String) data.get("route");
            latLngs = PolyUtil.decode(encoded);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
