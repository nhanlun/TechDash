package com.example.techdash.models;

public class User {
    private String uid;
    private String name;
    private int energy;


    public User(String uid, String name, int energy) {
        this.uid = uid;
        this.name = name;
        this.energy = energy;
    }

    public String getUid() {
        return uid;
    }
}
