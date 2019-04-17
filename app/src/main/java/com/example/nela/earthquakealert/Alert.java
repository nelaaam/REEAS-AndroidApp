package com.example.nela.earthquakealert;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Alert extends Activity {
    private static final String TAG = "ALERT";
    private static final String ALERT_ACTIVITY = "Running";
    private static final String SHARED_PREFS = "Shared_Prefs";
    private static final String LATITUDE = "Latitude";
    private static final String LONGITUDE = "Longitude";
    private WindowManager windowManager;
    private View alertView;
    private TextView magnitude, location, timerView;
    private int min, sec, eStart;
    private boolean timerRunning = false;
    private CountDownTimer countDownTimer;
    private long counter;
    private String mag, loc, alertTime, event_start;
    private double elat, elong, clat, clong, distance, count;
    private BroadcastReceiver UpdateUI;
    private Date start, now;
    private Bundle bundle;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alertView = LayoutInflater.from(this).inflate(R.layout.alert_popoup, null, false);
        magnitude = alertView.findViewById(R.id.alert_magnitude);
        location = alertView.findViewById(R.id.alert_location);
        timerView = alertView.findViewById(R.id.alert_timer);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, 0);
        editor = sharedPreferences.edit();
        editor.putBoolean(ALERT_ACTIVITY, true);
        editor.apply();
        bundle = getIntent().getExtras();
        Alert.this.runOnUiThread(new Runnable() {
            public void run() {
                startAlert();
                IntentFilter filter = new IntentFilter();
                filter.addAction("com.alert.broadcast");
                UpdateUI = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String action = intent.getAction();
                        Log.i(TAG, "Broadcast received: " + action);
                        if(action.equals("com.alert.broadcast")){
                            Log.d(TAG, "Hello");
                        }
                        Log.d(TAG, "HELLO");
                        if (timerRunning && sharedPreferences.getBoolean(ALERT_ACTIVITY, false)) {
                            countDownTimer.cancel();
                        }
                        bundle = new Bundle();
                        bundle = intent.getExtras();
                        sharedPreferences = getSharedPreferences(SHARED_PREFS, 0);
                        Log.d(TAG, "Magnitude = " + bundle.getString("Magnitude"));
                        startAlert();
                    }
                };
                localBroadcastManager.registerReceiver(UpdateUI, filter);




                WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT
                );
                params.gravity = Gravity.CENTER;
                windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

                windowManager.addView(alertView, params);
                Button close_button = alertView.findViewById(R.id.close_button);
                close_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (windowManager != null) {
                            windowManager.removeView(alertView);
                        }
                        if (timerRunning) {
                            countDownTimer.cancel();
                            Log.d(TAG, "ALERT IS CANCELLED");
                        }
                        editor.putBoolean(ALERT_ACTIVITY, false);
                        editor.apply();
                        Log.d(TAG, "ALERT IS " + sharedPreferences.getBoolean(ALERT_ACTIVITY, false));
                        finish();
                    }
                });
            }

        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (windowManager != null) {
            windowManager.removeView(alertView);
        }
        editor.putBoolean(ALERT_ACTIVITY, false);
        editor.apply();
        Log.d(TAG, "ALERT IS " + sharedPreferences.getBoolean(ALERT_ACTIVITY, false));
        localBroadcastManager.unregisterReceiver(UpdateUI);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(counter, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                counter = millisUntilFinished;
                min = (int) (counter / 1000) / 60;
                sec = (int) (counter / 1000) % 60;

                String timeLeftFormatted = String.format(Locale.getDefault(), "%01d:%02d", min, sec);
                timerView.setText(timeLeftFormatted);
                magnitude.setText(mag);
                location.setText(loc);
            }

            @Override
            public void onFinish() {
                if (windowManager != null) {
                    windowManager.removeView(alertView);
                } else {
                    editor.putBoolean(ALERT_ACTIVITY, false);
                    editor.apply();
                }
                if (timerRunning) {
                    countDownTimer.cancel();
                    timerRunning = false;
                }
                Log.d(TAG, "ALERT IS DONE");
                finish();
            }
        }.start();
        timerRunning = true;
    }

    public double calculateDistance(double elat, double elong, double clat, double clong) {
        final double R = 6371e3;
        double elatRad, clatRad, latDiff, longDiff, d, a, c;

        elatRad = Math.toRadians(elat);
        clatRad = Math.toRadians(clat);
        latDiff = Math.toRadians(elat - clat);
        longDiff = Math.toRadians(elong - clong);

        a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) + Math.cos(elatRad) * Math.cos(clatRad) * Math.sin(longDiff / 2) * Math.sin(longDiff / 2);
        c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        d = (R * c);
        return d/1000;
    }

    public double calculatePArrival(double distance, int speed, int eStart) {
        double timeLeftPArrival;
        timeLeftPArrival = (distance / speed) - eStart;
        Log.d(TAG, "P-Arrival = " + timeLeftPArrival);
        if(timeLeftPArrival < 0) return 0;
        else return timeLeftPArrival;
    }

    public double calculateSArrival(double distance, int speed, int eStart) {
        double timeLeftPArrival;
        timeLeftPArrival = ((distance / speed) - eStart);

        return timeLeftPArrival;
    }

    public void startAlert() {
        magnitude.setText(mag);
        location.setText(loc);
        mag = bundle.getString("Magnitude");
        loc = bundle.getString("Epicenter");
        elat = Double.valueOf(bundle.getString("Latitude"));
        elong = Double.valueOf(bundle.getString("Longitude"));
        clat = Double.valueOf(sharedPreferences.getString(LATITUDE, "0.00"));
        clong = Double.valueOf(sharedPreferences.getString(LONGITUDE, "0.00"));
        event_start = bundle.getString("Datetime");

        alertTime = sharedPreferences.getString("ALERT_TIME", null);
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Manila"));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
            now = new Date();
            alertTime = dateFormat.format(now);
            editor.putString("ALERT_TIME", alertTime);
            editor.apply();
            start = dateFormat.parse(event_start);
            eStart = (int) (now.getTime() - start.getTime())/1000;
            if (eStart < 0) killActivity();
            Log.d(TAG, "Time Earthquake has started = " + eStart);
        } catch (Exception e) {
            e.printStackTrace();
        }
        distance = calculateDistance(elat, elong, clat, clong);
        count = calculatePArrival(distance, 6, eStart);
        counter = Math.round(count) * 1000;
        Log.d(TAG, "Counter = " + counter);
        startTimer();

    }
    private void killActivity() {
        finish();
    }
}
