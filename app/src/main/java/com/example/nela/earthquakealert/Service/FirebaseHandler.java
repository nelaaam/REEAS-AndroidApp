package com.example.nela.earthquakealert.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.nela.earthquakealert.Alert;
import com.example.nela.earthquakealert.Dashboard;
import com.example.nela.earthquakealert.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class FirebaseHandler extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessaging";
    private static final String ALERT_ACTIVITY = "Running";
    private static final String SHARED_PREFS = "Shared_Prefs";
    private String datetime, date, time, latitude, longitude, epicenter, magnitude;
    private Intent intent = new Intent();
    private Bundle bundle = new Bundle();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Data message payload " + remoteMessage.getData());
            try {
                JSONObject obj = new JSONObject(remoteMessage.getData());
                datetime = obj.getString("datetime");
                String[] str1 = datetime.split("T");
                date = str1[0];
                String[] str2 = str1[1].split("Z");
                time = str2[0];
                latitude = obj.getString("latitude");
                longitude = obj.getString("longitude");
                epicenter = obj.getString("location");
                magnitude = obj.getString("magnitude");
                intent.setAction("com.alert.broadcast");
                intent.setClass(FirebaseHandler.this, Alert.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                bundle.putString("Datetime", datetime);
                bundle.putString("Latitude", latitude);
                bundle.putString("Longitude", longitude);
                bundle.putString("Epicenter", epicenter);
                bundle.putString("Magnitude", magnitude);

                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, 0);
              /* SharedPreferences.Editor editor = sharedPreferences.edit();
                editor = sharedPreferences.edit();
                editor.putBoolean(ALERT_ACTIVITY, false);
                editor.apply();*/
                boolean alertIsRunning = sharedPreferences.getBoolean(ALERT_ACTIVITY, false);
                Log.d(TAG, "ALERT IS " + sharedPreferences.getBoolean(ALERT_ACTIVITY, false));

                if (alertIsRunning) {
                    intent.replaceExtras(bundle);
                    Log.d(TAG, "" + bundle.getString("Magnitude"));
                    LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
                    localBroadcastManager.sendBroadcast(intent);
                } else {
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Notification message payload " + remoteMessage.getNotification());
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();
            String click_action = remoteMessage.getNotification().getClickAction();

            Log.d(TAG, "Message Notification Title: " + title);
            Log.d(TAG, "Message Notification Body: " + message);
            Log.d(TAG, "Message Notification Click_action: " + click_action);
            if (click_action != null) {
                sendNotification(title, message, click_action);
            } else {
                sendNotification(title, message);
            }
        }
    }

    private void sendNotification(String title, String messageBody, String click_action) {
        Intent intent;
        if (click_action.equals("Dashboard")) {
            intent = new Intent(getApplicationContext(), Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
            intent = new Intent(getApplicationContext(), Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

    }

    private void sendNotification(String title, String messageBody) {

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("TOKENFIREBASE", s);
    }

}
