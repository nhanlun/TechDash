package com.example.techdash.models;

import java.net.URL;

public class Record {
    String date;
    Route route;
    URL url;

    public Record(String date, Route route, URL url) {
        this.date = date;
        this.route = route;
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}
