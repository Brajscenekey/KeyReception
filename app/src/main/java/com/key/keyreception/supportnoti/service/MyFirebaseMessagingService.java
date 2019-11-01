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
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.key.keyreception.R;
import com.key.keyreception.Session;
import com.key.keyreception.activity.ChattingActivity;
import com.key.keyreception.activity.DetailActivity;
import com.key.keyreception.activity.LoginActivity;
import com.key.keyreception.activity.owner.OwnerTabActivity;
import com.key.keyreception.activity.recepnist.AppoDetailActivity;
import com.key.keyreception.activity.recepnist.TabActivity;

import java.util.List;

import static com.key.keyreception.helper.Constant.Other_User_id;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    //    String title = "Notification!";

    String CHANNEL_ID = "com.hours.labors";// The id of the channel.
    String title = "";
    String body = "";
    String notifincationType = "";
    String notifyId = "";
    String userType = "";
    String forUserType = "";
    String opponentChatId = "";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

         Log.e(TAG, "Body: " + remoteMessage.getData().toString());
        Log.e(TAG, "Body1: " + remoteMessage);


//        Bundle[{other_key=true,
//                gcm.notification.fcm_token=eTxjnSR7j3c:APA91bG3Jq7rwaYOjsLuGEiyo2iW1ctZPmCuhbCgyGUBRpLXpQ1Jh_2u5ziAuQb-rn2Qxe413C-IUMej9_FQWl2XoJhMleSZhFQLUExwqg__xA_Mdyns-Z7PnhwVPdZaTSmWU6VNGjyz,
//                google.delivered_priority=high,
//                google.sent_time=1558501298273,
//                google.ttl=2419200,
//                google.original_priority=high,
//                gcm.notification.e=1,
//                gcm.notification.other_key=true,
//                priority=high, opponentChatId=5,
//                ChatTitle=vin diesal,
//                gcm.notification.badge=1,
//                gcm.notification.sound=default,
//        gcm.notification.title=vin diesal,
//                username=vin diesal,
//                gcm.notification.ChatTitle=vin diesal,
//                uid=5, body=Hello ,
//                from=21884075450,
//                icon=new, type=chat,
//                gcm.notification.sound2=default,
//                gcm.notification.message=Hello ,
//                badge=1, sound=default,
//                title=vin diesal, click_action=ChatActivity,
//                google.message_id=0:1558501298279621%e045cf4ce045cf4c,
//                gcm.notification.body=Hello ,
//                gcm.notification.icon=new,
//                gcm.notification.type=chat,
//                gcm.notification.opponentChatId=5,
//                message=Hello , google.c.a.e=1,
//                gcm.notification.uid=5,
//                content_available=true,
//                gcm.notification.click_
//                action=ChatActivity,
//                gcm.notification.username=vin diesal,
//                fcm_token=eTxjnSR7j3c:APA91bG3Jq7rwaYOjsLuGEiyo2iW1ctZPmCuhbCgyGUBRpLXpQ1Jh_2u5ziAuQb-rn2Qxe413C-IUMej9_FQWl2XoJhMleSZhFQLUExwqg__xA_Mdyns-Z7PnhwVPdZaTSmWU6VNGjyz, collapse_key=com.key.keyreception}]


//        Bundle[{google.delivered_priority=high,
//                google.sent_time=1557305169835,
//                google.ttl=2419200,
//                google.original_priority=high,
//                gcm.notification.e=1,
//                gcm.notification.fullName=shiva,
//                gcm.notification.notifincationType=1,
//                gcm.notification.notifyId=7,
//                gcm.notification.sound=default,
//        gcm.notification.title=Job Request,
//                userType=receptionist,
//                body=shiva sent you a job request.,
//                from=21884075450, gcm.notification.sound2=default,
//        title=Job Request, click_action=ChatActivity,
//                google.message_id=0:1557305169847650%e045cf4ce045cf4c,
//                gcm.notification.body=shiva sent you a job request.,
//                notifincationType=1, forUserType=receptionist,
//                google.c.a.e=1, fullName=shiva,
//                notifyId=7, gcm.notification.click_action=ChatActivity,
//                gcm.notification.userType=receptionist,
//                collapse_key=com.key.keyreception,
//                gcm.notification.forUserType=receptionist}]


        if (remoteMessage.getData().containsKey("body")) {
            title = remoteMessage.getData().get("title");
            body = remoteMessage.getData().get("body");
            userType = remoteMessage.getData().get("userType");
            notifincationType = remoteMessage.getData().get("notifincationType");
            opponentChatId = remoteMessage.getData().get("opponentChatId");


            forUserType = remoteMessage.getData().get("forUserType");
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
                    if (sessionManager.getusertype().equals(forUserType)) {
                        intent = new Intent("BroadcastNotification");
                        intent.putExtra("notifyId", notifyId);
                        sendBroadcast(intent);
                    } else {
                        intent = new Intent(this, TabActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("checkid", "1001");
                    }
                } else {
                    new Intent(this, LoginActivity.class);
                }

                break;
            case "2":

                if (sessionManager.isLoggedIn()) {
                    if (sessionManager.getusertype().equals(forUserType)) {
                        intent = new Intent(this, DetailActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("notifyId", notifyId);
                    } else {

                        intent = new Intent(this, TabActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("checkid", "1001");
                    }


                } else {
                    new Intent(this, LoginActivity.class);
                }

                sendNotification(remoteMessage.getTtl(), title, intent, body, false);


                break;

            case "3":
                if (sessionManager.isLoggedIn()) {
                    if (sessionManager.getusertype().equals(forUserType)) {
                        intent = new Intent(this, DetailActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("notifyId", notifyId);
                    } else {

                        intent = new Intent(this, TabActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("checkid", "1001");
                    }

                } else {
                    new Intent(this, LoginActivity.class);
                }
                sendNotification(remoteMessage.getTtl(), title, intent, body, false);

                break;

            case "4":
                if (sessionManager.isLoggedIn()) {
                    if (sessionManager.getusertype().equals(forUserType)) {
                        intent = new Intent(this, DetailActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("notifyId", notifyId);
                    } else {

                        intent = new Intent(this, TabActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("checkid", "1001");
                    }

                } else {
                    new Intent(this, LoginActivity.class);
                }
                sendNotification(remoteMessage.getTtl(), title, intent, body, false);

                break;

            case "5":
                if (sessionManager.isLoggedIn()) {
                    if (sessionManager.getusertype().equals(forUserType)) {
                        intent = new Intent(this, AppoDetailActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("notifyId", notifyId);
                    } else {

                        intent = new Intent(this, OwnerTabActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("checkid", "1001");
                    }

                } else {
                    new Intent(this, LoginActivity.class);
                }
                sendNotification(remoteMessage.getTtl(), title, intent, body, false);

                break;

            case "chat":

                if (Other_User_id != Integer.parseInt(opponentChatId)) {
                    if (sessionManager.isLoggedIn()) {
                        intent = new Intent(this, ChattingActivity.class);
                        intent.putExtra("serviceProviderId", opponentChatId);
                    } else {
                        new Intent(this, LoginActivity.class);
                    }
                    sendNotification(remoteMessage.getTtl(), title, intent, body, false);
                }
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