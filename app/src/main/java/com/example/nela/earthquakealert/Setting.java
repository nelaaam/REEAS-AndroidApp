/*package com.example.nela.earthquakealert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;


import com.google.firebase.messaging.FirebaseMessaging;

public class Setting extends AppCompatActivity {
    Boolean vibration;
    Switch alertSwitch, alertSound, alertVibrate, alertNotification;
    int x;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCH1 = "alertSwitch";
    public static final String SWITCH2 = "alertSound";
    public static final String SWITCH3 = "alertVibrate";
    public static final String SWITCH4 = "alertNotification";

    public boolean SW1,SW2,SW3,SW4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        alertSwitch = findViewById(R.id.alert_switch);
        alertSound = findViewById(R.id.alert_sounds);
        alertVibrate = findViewById(R.id.alert_vibrate);
        alertNotification = findViewById(R.id.alert_notif);


        alertSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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


        //alertSwitch.setOnClickListener(new View.OnClickListener() {
           // @Override
           // public void onClick(View v) {
               // boolean checked = alertSwitch.isChecked();
               // setFCMTopic(checked);
           // }
        //});

        //alertSound.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View v) {
                //boolean checked = alertSound.isChecked();
                //if (checked) {
                    //TODO: Enable sounds
                //} else {
                    //TODO: Disable Sounds
               // }
           // }
        //});
        //alertVibrate.setOnClickListener(new View.OnClickListener() {
           // @Override
            //public void onClick(View v) {
               // boolean checked = alertVibrate.isChecked();
                //if (checked) {
                    //setVibration(true);
                //} else {
                    //setVibration(false);
                //}
            //}
        //});

        });

       // loadData();
       // updatePref();
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SWITCH1, alertSwitch.isChecked());
        editor.putBoolean(SWITCH2, alertSound.isChecked());
        editor.putBoolean(SWITCH3, alertVibrate.isChecked());
        editor.putBoolean(SWITCH4, alertNotification.isChecked());

        editor.apply();

        Toast.makeText(this, "Changes applied",Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SW1 = sharedPreferences.getBoolean(SWITCH1, false);
        SW2 = sharedPreferences.getBoolean(SWITCH2, false);
        SW3 = sharedPreferences.getBoolean(SWITCH3, false);
        SW4 = sharedPreferences.getBoolean(SWITCH4, false);
    }

    public void updatePref() {
        alertSwitch.setChecked(SW1);
        alertSound.setChecked(SW2);
        alertVibrate.setChecked(SW3);
        alertNotification.setChecked(SW4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_about:
                startActivity(new Intent(this, About.class));
                return true;

            case R.id.action_settings:
                startActivity(new Intent(this, Setting.class));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void setVibration(boolean v) {
        vibration = v;
    }

    public boolean getVibration() {
        return vibration;
    }

    //public void setFCMTopic(boolean c) {
   //     if (c) {
       //     FirebaseMessaging.getInstance().subscribeToTopic("ALERT_NOTIFICATIONS");
      //      alertSound.setEnabled(true);
        //    alertVibrate.setEnabled(true);
      //  } else {
      //      FirebaseMessaging.getInstance().subscribeToTopic("NO_NOTIFICATIONS");
       //     alertSound.setEnabled(false);
       //     alertVibrate.setEnabled(false);
        //}
    //}
}
*/