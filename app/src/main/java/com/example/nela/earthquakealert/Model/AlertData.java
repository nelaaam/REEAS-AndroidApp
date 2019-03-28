package com.example.nela.earthquakealert.Model;

public class AlertData {

    private String date, time, latitude, longitude, speed, epicenter, magnitude, intensity;

    public AlertData(String date, String time, String latitude, String longitude, String speed, String epicenter, String magnitude, String intensity) {
        this.date = date;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.epicenter = epicenter;
        this.magnitude = magnitude;
        this.intensity = intensity;
    }

    public String getDate() {
        return date;
    }

    public String getTime() { return time; }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getSpeed() {
        return speed;
    }

    public String getEpicenter() { return epicenter; }

    public String getMagnitude() {
        return magnitude;
    }

    public String getIntensity() { return intensity; }
}
