package com.example.nela.earthquakealert.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.nela.earthquakealert.Alert;
import com.example.nela.earthquakealert.Dashboard;
import com.example.nela.earthquakealert.Model.AlertData;
import com.example.nela.earthquakealert.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class FirebaseHandler extends FirebaseMessagingService {

    //TODO: Handle activity when open after receiving notification
    private static final String TAG = "FirebaseMessaging";
    private View alertView;
    AlertData d;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Data message payload " + remoteMessage.getData());
            try {
                JSONObject obj = new JSONObject(remoteMessage.getData());
                d = new AlertData(obj.getString("Date"), obj.getString("Time"), obj.getString("Latitude"), obj.getString("Longitude"), obj.getString("Speed"), obj.getString("Epicenter"), obj.getString("Magnitude"), obj.getString("Intensity"));
                Log.d(TAG, "Magnitude = " + d.getMagnitude());
                //TODO: Handle Data for updating alert popup
            } catch(JSONException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            intent.setAction("com.hello.action");
            intent.setClass(FirebaseHandler.this, Alert.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            bundle.putString("Magnitude",d.getMagnitude());
            bundle.putString("Latitude",d.getLatitude());
            bundle.putString("Longitude",d.getLongitude());
            intent.putExtras(bundle);

//            startService(new Intent(FirebaseHandler.this, Alert.class));

            startActivity(intent);
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Notification message payload " + remoteMessage.getNotification());
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();
            String click_action = remoteMessage.getNotification().getClickAction();

            Log.d(TAG, "Message Notification Title: " + title);
            Log.d(TAG, "Message Notification Body: " + message);
            Log.d(TAG, "Message Notification Click_action: " + click_action);

            sendNotification(title, message, click_action);
        }
    }

    private void sendNotification (String title, String messageBody, String click_action){
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
    @Override
    public void onNewToken (String s){
        super.onNewToken(s);
        Log.d("TOKENFIREBASE", s);
    }

}
