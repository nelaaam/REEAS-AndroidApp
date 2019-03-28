package com.example.nela.earthquakealert.Model;

public class EventsData {

    private String date, location, hypocenter, time, magnitude;

    public EventsData(String date, String location, String hypocenter, String time, String magnitude) {
        this.date = date;
        this.location = location;
        this.hypocenter = hypocenter;
        this.time = time;
        this.magnitude = magnitude;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getHypocenter() {
        return hypocenter;
    }

    public String getTime() {
        return time;
    }

    public String getMagnitude() {
        return magnitude;
    }
}
