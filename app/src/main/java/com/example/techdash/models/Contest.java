package com.example.techdash.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.techdash.viewmodels.UserViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Contest implements Parcelable {
    private String id;
    private String destination;
    private String name;
    private String creator_username;
    private String startTime;
    private String endTime;
    private ArrayList<String> participants;

    public Contest() {}

    public Contest(Map<String, Object> data) {
        try {
            id = (String) data.get("id");
            destination = (String) data.get("destination");
            name = (String) data.get("name");
            startTime = (String) data.get("starttime");
            endTime = (String) data.get("endtime");
            creator_username = (String) data.get("creator");
            participants = (ArrayList<String>) data.get("participants");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public Contest(String id, String destination, String name, String creator_username, String startTime, String endTime, ArrayList<String> participants) {
        this.id = id;
        this.destination = destination;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.creator_username = creator_username;
        this.participants = participants;
    }

    protected Contest(Parcel in) {
        id = in.readString();
        destination = in.readString();
        name = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        creator_username = in.readString();
        in.readStringArray(participants.toArray(new String[0]));
    }

    public static final Creator<Contest> CREATOR = new Creator<Contest>() {
        @Override
        public Contest createFromParcel(Parcel in) {
            return new Contest(in);
        }

        @Override
        public Contest[] newArray(int size) {
            return new Contest[size];
        }
    };

    public String getID() { return id; }

    public void setID(String value) { this.id = value; }

    public String getDestination() { return destination; }

    public void setDestination(String value) { this.destination = value; }

    public String getCreator() { return creator_username; }

    public void setCreator(String value) { this.creator_username = value; }

    public String getName() { return name; }

    public void setName(String value) { this.name = value; }

    public String getStartTime() { return startTime; }

    public void setStartTime(String value) { this.startTime = value; }

    public String getEndTime() { return endTime; }

    public void setEndTime(String value) { this.endTime = value; }

    public ArrayList<String> getParticipants() { return participants; }

    public void addParticipants(String value) { this.participants.add(value); }

    public void removeParticipants(String value) { this.participants.remove(participants.indexOf(value)); }

    public boolean checkParticipant(String uid) {
        for (String id : participants) {
            if (uid.equals(id)) return true;
        }
        return false;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(destination);
        dest.writeString(name);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(creator_username);
        dest.writeStringArray(participants.toArray(new String[0]));
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("destination", destination);
        map.put("name", name);
        map.put("starttime", startTime);
        map.put("endtime", endTime);
        map.put("creator", creator_username);
        map.put("participants", participants);
        return map;
    }
}
