package com.example.nela.earthquakealert.Service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

public class GPSTracker extends Service {
    private Context context;
    private Location location;
    LocationListener locationListener;
    LocationManager locationManager;
    private static final String TAG = "GPSSERVICE";
    private static final String SHARED_PREFS = "Shared_Prefs";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITDE = "Longitude";

    public GPSTracker() {
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Intent i = new Intent("location_update");
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(LATITUDE, "" + location.getLatitude());
                editor.putString(LONGITDE, "" + location.getLongitude());
                editor.apply();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);

    }

    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

