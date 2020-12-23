package com.example.techdash.models;

public class User {
    private String uid;
    private String name;

    public User(String uid, String name, int energy) {
        this.uid = uid;
        this.name = name;
        this.energy = energy;
    }

    private int energy;
}
