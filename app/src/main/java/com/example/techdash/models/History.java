package com.example.techdash.models;

import android.util.Log;

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

    public List<LatLng> getLatLngs() {
        return latLngs;
    }

    public void setLatLngs(List<LatLng> latLngs) {
        this.latLngs = latLngs;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public double getPace() {
        return pace;
    }

    public void setPace(double pace) {
        this.pace = pace;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
