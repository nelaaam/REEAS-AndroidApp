package com.example.nela.earthquakealert;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;


public class SettingsActivity extends AppCompatActivity {
    private static final String ALERTS_ENABLED = "Alert_Enabled";
    private static final String SOUNDS_ENABLED = "Sounds_Enabled";
    private static final String VIBRATOR_ENABLED = "Vibrator_Enabled";
    private static final String NOTIFICATIONS_ENABLED = "Notifications_Enabled";
    private static final String TURNED_ON = "turned on.";
    private static final String TURNED_OFF = "turned off.";
    SwitchCompat alertSwitch, alertSound, alertVibrate, alertNotification;
    boolean alertEnabled, soundEnabled, vibEnabled, notifEnabled;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences("Shared_Prefs", 0);
        editor = sharedPreferences.edit();

        alertEnabled = sharedPreferences.getBoolean(ALERTS_ENABLED, true);
        soundEnabled = sharedPreferences.getBoolean(SOUNDS_ENABLED, false);
        vibEnabled = sharedPreferences.getBoolean(VIBRATOR_ENABLED, false);
        notifEnabled = sharedPreferences.getBoolean(NOTIFICATIONS_ENABLED, true);

        alertSwitch = findViewById(R.id.alert_switch);
        alertSound = findViewById(R.id.alert_sounds);
        alertVibrate = findViewById(R.id.alert_vibrate);
        alertNotification = findViewById(R.id.alert_notif);

        alertSwitch.setChecked(alertEnabled);
        alertSound.setChecked(soundEnabled);
        alertVibrate.setChecked(vibEnabled);
        alertNotification.setChecked(notifEnabled);

        if(!alertEnabled) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("ALERT");
            alertSound.setEnabled(false);
            alertVibrate.setEnabled(false);
            editor.putBoolean(ALERTS_ENABLED, false);
            editor.putBoolean(SOUNDS_ENABLED, false);
            editor.putBoolean(VIBRATOR_ENABLED, false);
            editor.apply();
        } else {
            FirebaseMessaging.getInstance().subscribeToTopic("ALERT");
            if (!alertSound.isEnabled()) {
                alertSound.setEnabled(true);
            }
            if (!alertVibrate.isEnabled()) {
                alertVibrate.setEnabled(true);
            }
            editor.putBoolean(ALERTS_ENABLED, true);
            editor.apply();
        }
        if (notifEnabled) {
            FirebaseMessaging.getInstance().subscribeToTopic("NOTIFICATIONS");
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("NOTIFICATIONS");
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
                    Toast.makeText(SettingsActivity.this, "Alert is " + TURNED_ON, Toast.LENGTH_SHORT).show();
                    FirebaseMessaging.getInstance().subscribeToTopic("ALERT");
                    alertSound.setEnabled(true);
                    alertVibrate.setEnabled(true);
                }
                else {
                    Toast.makeText(SettingsActivity.this, "Notification is " + TURNED_OFF, Toast.LENGTH_SHORT).show();
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
            }
        });

        alertSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    soundEnabled = !soundEnabled;
                    alertSound.setChecked(soundEnabled);
                    editor.putBoolean(SOUNDS_ENABLED, soundEnabled);
                    editor.apply();
                    if(sharedPreferences.getBoolean(SOUNDS_ENABLED, false)) {
                        Toast.makeText(SettingsActivity.this, "Vibrate on alert is " + TURNED_ON, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SettingsActivity.this, "Vibrate on alert is " + TURNED_OFF, Toast.LENGTH_SHORT).show();
                    }
            }
        });

        alertVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    vibEnabled = !vibEnabled;
                    alertVibrate.setChecked(vibEnabled);
                    editor.putBoolean(VIBRATOR_ENABLED, vibEnabled);
                    editor.apply();
                    if(sharedPreferences.getBoolean(VIBRATOR_ENABLED, false)) {
                        Toast.makeText(SettingsActivity.this, "Vibrate on alert is " + TURNED_ON, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SettingsActivity.this, "Vibrate on alert is " + TURNED_OFF, Toast.LENGTH_SHORT).show();
                    }
            }
        });

        alertNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                notifEnabled = !notifEnabled;
                alertSwitch.setChecked(notifEnabled);
                editor.putBoolean(NOTIFICATIONS_ENABLED, notifEnabled);
                editor.apply();
                if(isChecked){
                    Toast.makeText(SettingsActivity.this, "Notification is " + TURNED_ON, Toast.LENGTH_SHORT).show();
                    FirebaseMessaging.getInstance().subscribeToTopic("NOTIFICATIONS");
                } else {
                    Toast.makeText(SettingsActivity.this, "Notification is " + TURNED_OFF, Toast.LENGTH_SHORT).show();
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("NOTIFICATIONS");
                }
            }
        });
    }
}

