package com.example.nela.earthquakealert.Model;

public class EventsData {

    private String date, time, latitude, longitude, location, magnitude;

    public EventsData(String date, String time, String latitude, String longitude, String location, String magnitude) {
        this.date = date;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.magnitude = magnitude;
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public String getTime() { return time; }

    public String getLocation() {
        return location;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getMagnitude() {
        return magnitude;
    }
}
