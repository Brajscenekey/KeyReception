package com.key.keyreception.supportnoti.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.key.keyreception.Activity.DetailActivity;
import com.key.keyreception.Activity.LoginActivity;
import com.key.keyreception.R;
import com.key.keyreception.Session;

import java.util.List;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    //    String title = "Notification!";

    String CHANNEL_ID = "com.hours.labors";// The id of the channel.
    String title = "";
    String body = "";
    String notifincationType = "";
    String notifyId = "";
    String userType = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "Body: " + remoteMessage.getData().toString());
        Log.e(TAG, "Body1: " + remoteMessage);

//        Bundle[{urlImageString=http://koobi.co.uk:3000/keyreception/uploads/profile/undefined,
        // google.delivered_priority=high,
        // google.sent_time=1554726699825,
        // google.ttl=2419200,
        // google.original_priority=high,
        // gcm.notification.e=1,
        // gcm.notification.
        // fullName=aarti,
        // gcm.notification.notifincationType=1,
        // gcm.notification.notifyId=54,
        // gcm.notification.sound=default,
        // gcm.notification.title=Job Request,
        // userType=owner,
        // body=aarti sent a job request.,
        // from=21884075450,
        // gcm.notification.sound2=default,
        // title=Job Request,
        // click_action =ChatActivity,
        // gcm.notification.urlImageString=http://koobi.co.uk:3000/keyreception/uploads/profile/undefined,
        // google.message_id=0:1554726699832972%e045cf4ce045cf4c,
        // gcm.notification.body=aarti sent a job request.,
        // notifincationType=1, google.c.a.e=1,
        // fullName=aarti, notifyId=54,
        // gcm.notification.
        // click_action=ChatActivity,
        // gcm.notification.userType=owner,
        // collapse_key=com.key.keyreception}]


        if (remoteMessage.getData().containsKey("body")) {
            title = remoteMessage.getData().get("title");
            body = remoteMessage.getData().get("body");
            userType = remoteMessage.getData().get("userType");
            notifincationType = remoteMessage.getData().get("notifincationType");
            if (remoteMessage.getData().get("notifyId") != null) {
                notifyId = remoteMessage.getData().get("notifyId");
            }

            //  Log.v("reference_id", reference_id);

        }

        Intent intent = null;
        Session sessionManager = new Session(this);

        switch (notifincationType) {
            case "1":

                if (sessionManager.isLoggedIn()) {
                    intent = new Intent("BroadcastNotification");
                    intent.putExtra("notifyId", notifyId);
                    sendBroadcast(intent);
                } else {
                    new Intent(this, LoginActivity.class);
                }

                break;
            case "2":
                if (sessionManager.isLoggedIn()) {
                    intent = new Intent(this, DetailActivity.class);
                    intent.putExtra("notifyId", notifyId);
                } else {
                    new Intent(this, LoginActivity.class);
                }
                sendNotification(remoteMessage.getTtl(), title, intent, body, false);

                break;
        }
    }

    //    27/dec/2018 Changes
    private void sendNotification(int id, String title, Intent intent, String messageBody, boolean send) {
        PendingIntent pendingIntent = null;
        NotificationCompat.Builder notificationBuilder;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        CharSequence name = "MyChannal";// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = null;

        if (!send) {

            notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

        } else {
            notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    // .setSmallIcon(R.drawable.icon_app_192_white)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setSmallIcon(R.drawable.logo)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.logo);
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
        } else {
            notificationBuilder.setSmallIcon(R.drawable.logo);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);
        }
        assert notificationManager != null;
        notificationManager.notify(1, notificationBuilder.build());
    }

    private boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }
}