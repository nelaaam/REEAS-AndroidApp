package com.example.nela.earthquakealert;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import com.google.firebase.messaging.FirebaseMessaging;


public class SettingsActivity extends AppCompatActivity {
    private static final String ALERTS_ENABLED = "Alert_Enabled";
    private static final String SOUNDS_ENABLED = "Alert_Enabled";
    private static final String VIBRATOR_ENABLED = "Alert_Enabled";
    private static final String NOTIFICATIONS_ENABLED = "Alert_Enabled";
    SwitchCompat alertSwitch, alertSound, alertVibrate, alertNotification;
    boolean alertEnabled, soundEnabled, vibEnabled, notifEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final SharedPreferences sharedPreferences = getSharedPreferences("PREFS", 0);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        alertEnabled = sharedPreferences.getBoolean(ALERTS_ENABLED, true);
        soundEnabled = sharedPreferences.getBoolean(SOUNDS_ENABLED, false);
        vibEnabled = sharedPreferences.getBoolean(VIBRATOR_ENABLED, false);
        notifEnabled = sharedPreferences.getBoolean(NOTIFICATIONS_ENABLED, true);

        alertSwitch = (SwitchCompat) findViewById(R.id.alert_switch);
        alertSound = (SwitchCompat) findViewById(R.id.alert_sounds);
        alertVibrate = (SwitchCompat) findViewById(R.id.alert_vibrate);
        alertNotification = (SwitchCompat) findViewById(R.id.alert_notif);

        //Initialization
        alertSwitch.setChecked(alertEnabled);
        alertSound.setChecked(soundEnabled);
        alertVibrate.setChecked(vibEnabled);
        alertNotification.setChecked(notifEnabled);
        if(!alertEnabled) {
            alertSound.setEnabled(false);
            alertVibrate.setEnabled(false);
        } else {
            FirebaseMessaging.getInstance().subscribeToTopic("ALERT");
            if (!alertSound.isEnabled()) {
                alertSound.setEnabled(true);
                editor.putBoolean(SOUNDS_ENABLED, false);
            }
            if (!alertVibrate.isEnabled()) {
                alertVibrate.setEnabled(true);
                editor.putBoolean(VIBRATOR_ENABLED, false);
            }
        }
        if (notifEnabled) {
            FirebaseMessaging.getInstance().subscribeToTopic("NOTIFICATIONS");
        }

        //Check change listener
        alertSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alertEnabled = !alertEnabled;
                alertSwitch.setChecked(alertEnabled);
                editor.putBoolean(ALERTS_ENABLED, alertEnabled);
                editor.apply();
                if(isChecked){
                    FirebaseMessaging.getInstance().subscribeToTopic("ALERT");
                    if (!alertSound.isEnabled()) {
                        alertSound.setEnabled(true);
                    }
                    if (!alertVibrate.isEnabled()) {
                        alertVibrate.setEnabled(true);
                    }
                }
                else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("ALERT");
                    if (alertSound.isEnabled()) {
                        alertSound.setEnabled(false);
                        alertSound.setChecked(false);
                        editor.putBoolean(SOUNDS_ENABLED, false);
                    }
                    if (alertVibrate.isEnabled()) {
                        alertVibrate.setEnabled(false);
                        alertVibrate.setChecked(false);
                        editor.putBoolean(VIBRATOR_ENABLED, false);
                    }
                    editor.apply();
                }
            };
        });

        alertSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (alertEnabled){
                    soundEnabled = !soundEnabled;
                    alertSound.setChecked(soundEnabled);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(SOUNDS_ENABLED, soundEnabled);
                    editor.apply();
                    //if (isChecked) {
                    //TODO: Enable sounds
                    //} else {
                    //TODO: Disable Sounds
                    // }
                } else {
                    alertSound.setEnabled(false);
                }


            };
        });

        alertVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (alertEnabled){
                    vibEnabled = !vibEnabled;
                    alertVibrate.setChecked(vibEnabled);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(VIBRATOR_ENABLED, vibEnabled);
                    editor.apply();
                    if (isChecked) {
                        //setVibration(true);
                        //TODO: Set vibration
                    }
                    else {
                        //setVibration(false);
                    }
                } else {
                    alertVibrate.setEnabled(false);
                }


            };
        });

        alertNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alertEnabled = !alertEnabled;
                alertSwitch.setChecked(alertEnabled);
                editor.putBoolean(ALERTS_ENABLED, alertEnabled);
                editor.apply();
                if(isChecked){
                    FirebaseMessaging.getInstance().subscribeToTopic("NOTIFICATIONS");
                } else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("NOTIFICATIONS");
                }


            };
        });
    }
}

