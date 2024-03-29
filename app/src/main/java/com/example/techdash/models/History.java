package com.example.techdash.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class History implements Parcelable {
    private ArrayList<LatLng> latLngs;
    private double distance;
    private long totalTime;
    private double pace;
    private String dateTime;
    private String id;

    public History() {

    }


    public History(Map<String, Object> data) {
        try {
            id = String.valueOf((long) data.get("time_in_millis"));
            distance = (double) data.get("distance");
            totalTime = (long) data.get("total_time");
            pace = (double) data.get("pace");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis((long) data.get("time_in_millis"));
            dateTime = calendar.getTime().toString();
            String encoded = (String) data.get("route");
            List<LatLng> tmp = PolyUtil.decode(encoded);
            latLngs = new ArrayList<>(tmp);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    protected History(Parcel in) {
        id = in.readString();
        latLngs = in.createTypedArrayList(LatLng.CREATOR);
        distance = in.readDouble();
        totalTime = in.readLong();
        pace = in.readDouble();
        dateTime = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeTypedList(latLngs);
        dest.writeDouble(distance);
        dest.writeLong(totalTime);
        dest.writeDouble(pace);
        dest.writeString(dateTime);
    }

    public static final Creator<History> CREATOR = new Creator<History>() {
        @Override
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };

    public String getId() {
        return id;
    }

    public ArrayList<LatLng> getLatLngs() {
        return latLngs;
    }

    public void setLatLngs(ArrayList<LatLng> latLngs) {
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
