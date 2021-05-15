package com.example.techdash.models;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String uid;
    private String name;
    private long energy;

    public User(Map<String, Object> map) {
        name = (String) map.get("name");
        uid = (String) map.get("uid");
        energy = (long) map.get("energy");
    }

    public User(String uid, String name, long energy) {
        this.uid = uid;
        this.name = name;
        this.energy = energy;
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

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("name", name);
        map.put("energy", energy);
        return map;
    }
}
