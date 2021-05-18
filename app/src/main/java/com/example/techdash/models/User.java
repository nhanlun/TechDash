package com.example.techdash.models;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String uid;
    private String name;
    private long energy;
    private double score;

    public User(Map<String, Object> map) {
        name = (String) map.get("name");
        uid = (String) map.get("uid");
        energy = (long) map.get("energy");
//        score = (double) map.get("score");
    }

    public User(String uid, String name, long energy) {
        this.uid = uid;
        this.name = name;
        this.energy = energy;
        this.score = 0;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public long getEnergy() {
        return energy;
    }

    public void setName(String _name) {
        name = _name;
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("name", name);
        map.put("energy", energy);
        map.put("score", score);
        return map;
    }
}
