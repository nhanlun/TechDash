package com.example.techdash.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Contest {
    private String id;
    private String destination;
    private String name;
    private String creator_username;
    private long startTime;
    private long endTime;
    private ArrayList<String> participants = new ArrayList<>();

    public Contest(Map<String, Object> map) {
        // TODO: get the attribute here
        try {
            id = (String) map.get("id");
            destination = (String) map.get("destination");
            name = (String) map.get("name");
            creator_username = (String) map.get("creator_username");
            startTime = (long) map.get("start_time");
            endTime = (long) map.get("end_time");
        } catch (Exception ignored) {
        }
    }

    public Contest() {
        // TODO: remove this
        participants.add("Who");
        participants.add("am");
        participants.add("I");
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public String getId() {
        // TODO: replace this with the real implementation
        return "abcxyz";
//        return id;
    }

    public HashMap<String, Object> toHashMap() {
        // TODO: replace this with the real implementation
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("destination", destination);
        map.put("name", name);
        map.put("creator_username", creator_username);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        return map;
    }
}