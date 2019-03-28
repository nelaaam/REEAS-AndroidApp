package com.example.nela.earthquakealert;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import com.google.firebase.messaging.FirebaseMessaging;


public class SettingsActivity extends AppCompatActivity {

    Boolean vibration;
    SwitchCompat alertSwitch, alertSound, alertVibrate, alertNotification;
    boolean SW1,SW2,SW3,SW4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final SharedPreferences sharedPreferences = getSharedPreferences("PREFS", 0);

        SW1 = sharedPreferences.getBoolean("SWITCH1", false);
        SW2 = sharedPreferences.getBoolean("SWITCH2", false);
        SW3 = sharedPreferences.getBoolean("SWITCH3", false);
        SW4 = sharedPreferences.getBoolean("SWITCH4", false);

        alertSwitch = (SwitchCompat) findViewById(R.id.alert_switch);
        alertSound = (SwitchCompat) findViewById(R.id.alert_sounds);
        alertVibrate = (SwitchCompat) findViewById(R.id.alert_vibrate);
        alertNotification = (SwitchCompat) findViewById(R.id.alert_notif);

        alertSwitch.setChecked(SW1);
        alertSound.setChecked(SW2);
        alertVibrate.setChecked(SW3);
        alertNotification.setChecked(SW4);


        alertSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SW1 = !SW1;
                alertSwitch.setChecked(SW1);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("SWITCH1", SW1);
                editor.apply();
                if(isChecked){
                    FirebaseMessaging.getInstance().subscribeToTopic("ALERT_NOTIFICATIONS");
                    alertSound.setEnabled(true);
                    alertVibrate.setEnabled(true);
                }
                else {
                    FirebaseMessaging.getInstance().subscribeToTopic("NO_NOTIFICATIONS");
                    alertSound.setEnabled(false);
                    alertVibrate.setEnabled(false);
                }

            };
        });

        alertSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SW2 = !SW2;
                alertSound.setChecked(SW2);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("SWITCH2", SW2);
                editor.apply();
                //if (isChecked) {
                //TODO: Enable sounds
                //} else {
                //TODO: Disable Sounds
                // }

            };
        });

        alertVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SW3 = !SW3;
                alertVibrate.setChecked(SW3);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("SWITCH3", SW3);
                editor.apply();
                if (isChecked) {
                    setVibration(true);
                }
                else {
                    setVibration(false);
                }

            };
        });
    }

    public void setVibration(boolean v) {
        vibration = v;
    }

    public boolean getVibration() {
        return vibration;
    }
}

