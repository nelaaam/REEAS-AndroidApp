package com.example.nela.earthquakealert.Service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class GPSTracker implements LocationListener {
    private Context context;
    private Location location;
    public GPSTracker(Context c) {
        context = c;
    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context,"Permission not granted",Toast.LENGTH_SHORT).show();
            return null;
        }
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(isGPSenabled) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 10, this);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return location;
        } else {

        }
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

