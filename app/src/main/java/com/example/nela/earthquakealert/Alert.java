package com.example.nela.earthquakealert;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Alert extends Activity {
    private static final String TAG = "ALERT";
    private WindowManager windowManager;
    private View alertView;
    private long counter = 131000;
    int min, sec;
    private boolean timerRunning = false;
    private CountDownTimer countDownTimer;

    String mag, loc;
    double elat, elong, clat, clong, distance;
    double count;
    private StringBuilder stringBuilder;
    private Geocoder geocoder;
    private BroadcastReceiver UpdateUI;
    private Bundle bundle;
    private TextView magnitude, location, timerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        alertView = LayoutInflater.from(this).inflate(R.layout.alert_popoup, null,false);
        magnitude = alertView.findViewById(R.id.alert_magnitude);
        location = alertView.findViewById(R.id.alert_location);
        timerView = alertView.findViewById(R.id.alert_timer);

        bundle = getIntent().getExtras();
        mag = bundle.getString("Magnitude");
        elat = Double.valueOf(bundle.getString("Latitude"));
        elong = Double.valueOf(bundle.getString("Longitude"));

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.hello.action");
        UpdateUI = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                bundle = getIntent().getExtras();
                mag = bundle.getString("Magnitude");
                elat = Double.valueOf(bundle.getString("Latitude"));
                elong = Double.valueOf(bundle.getString("Longitude"));
                magnitude.setText(mag);
                location.setText(loc);
            }
        };
        registerReceiver(UpdateUI,filter);


      /*  locator = new GPSTracker(getApplicationContext());
        Location l = locator.getLocation();
        clat = l.getLatitude();
        clong = l.getLongitude();

        geocoder = new Geocoder(this, Locale.getDefault());
        stringBuilder = new StringBuilder();
        getCityName();
        Log.d(TAG, "Latitude: " + elat);
        Log.d(TAG, "Longitude: " + elong);
        Log.d(TAG, "Geocoder says: " + loc);*/




        distance = calculateDistance(elat, elong, clat, clong);
        count = calculatePArrival(distance, 4, 5000);
        magnitude.setText(mag);
        location.setText(loc);

        min = (int) count / 60;
        sec = (int) count % 60;

        startTimer();

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY ,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.CENTER;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(alertView, params);

        Button  close_button = alertView.findViewById(R.id.close_button);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Button is clicked.");
                if (windowManager != null) {
                    windowManager.removeView(alertView);
                }
                if (timerRunning) {
                    countDownTimer.cancel();
                }
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(UpdateUI);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(counter, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                counter = millisUntilFinished;
                min = (int) (counter / 1000) / 60;
                sec = (int) (counter / 1000) % 60;

                String timeLeftFormatted = String.format(Locale.getDefault(),"%01d:%02d", min, sec);

                timerView.setText(timeLeftFormatted);
            }

            @Override
            public void onFinish() {
                if (windowManager != null) {
                    windowManager.removeView(alertView);
                }
                if (timerRunning) {
                    countDownTimer.cancel();
                    timerRunning = false;
                }
                finish();
            }
        }.start();
        timerRunning = true;
    }
    private void getCityName() {
        try {
            List<Address> address = geocoder.getFromLocation(elat, elong, 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i=0; i < maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                stringBuilder.append(addressStr);
                stringBuilder.append(" ");
            }

            loc = stringBuilder.toString(); //This is the complete address.
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
        int x=0;
        while(x != 10 && loc != null){
            getCityName();
            x++;
        }
    }
    public double calculateDistance(double elat, double elong, double clat, double clong) {
        final double R = 6371e3;
        double elatRad, clatRad, latDiff, longDiff, d, a, c;

        elatRad = Math.toRadians(elat);
        clatRad = Math.toRadians(clat);
        latDiff = Math.toRadians(elat-clat);
        longDiff = Math.toRadians(elong-clong);

        a = Math.sin(latDiff/2) * Math.sin(latDiff/2) + Math.cos(elatRad) * Math.cos(clatRad) * Math.sin(longDiff/2) * Math.sin(longDiff/2);
        c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        d = (R*c);
        return d;
    }
    public double calculatePArrival(double distance, int speed, int eStart) {
        double timeLeftPArrival;
        timeLeftPArrival =((distance/speed) - eStart)/1000;
        return timeLeftPArrival;
    }
    public double calculateSArrival(double distance, int speed, int eStart) {
        double timeLeftPArrival;
        timeLeftPArrival =((distance/speed) - eStart)/1000;
        return timeLeftPArrival;
    }
}
