package com.theacecoder.task2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Archit on 27-07-2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private long[] vibration = {0, 200, 0, 300};
    LocalBroadcastManager localBroadcastManager;
    private MySharedPreference mySharedPreference;
    static final public String COPA_RESULT = "com.controlj.copame.backend.COPAService.REQUEST_PROCESSED";

    @Override
    public void onCreate() {
        super.onCreate();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            mySharedPreference = new MySharedPreference(this);
            String chattoken = remoteMessage.getData().get("mytoken");
            mySharedPreference.saveChatToken(chattoken);
            String a = remoteMessage.getData().get("author");
            String m = remoteMessage.getData().get("message");
            Chat c = new Chat(m,a);
            MainActivity.getInstance().getChats().add(c);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        if(mySharedPreference.getChatStatus()) {
            Intent intent = new Intent(COPA_RESULT);
            localBroadcastManager.sendBroadcast(intent);
            //ChatActivity.getInstance().getChatAdapter().notifyDataSetChanged();
        }
        else {
            sendNotification(remoteMessage.getNotification().getBody());
            mySharedPreference.setFromNotification(true);
        }
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(vibration)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
