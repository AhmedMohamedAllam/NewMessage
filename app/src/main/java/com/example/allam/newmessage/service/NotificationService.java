package com.example.allam.newmessage.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.allam.newmessage.R;
import com.example.allam.newmessage.activity.MessagesActivity;
import com.example.allam.newmessage.database.DatabaseQueries;
import com.example.allam.newmessage.model.Message;

public class NotificationService extends IntentService {
    private static final String TAG = "notificationService";

    public static Intent newIntent(Context context) {
        return new Intent(context, NotificationService.class);
    }

    public NotificationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getExtras() != null) {
            Message message = (Message) intent.getExtras().getSerializable("message");
            if (message != null) {
                pushNotification(message.getUser(), message.getBody());
                DatabaseQueries database = new DatabaseQueries(getBaseContext());
                database.addMessage(message);
            }
        }
    }

    /**
     * @param ContentTitle the title of the notification
     * @param ContentText  the body message of the notification
     */
    public void pushNotification(String ContentTitle, String ContentText) {
        Intent i = new Intent(NotificationService.this, MessagesActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(ContentTitle)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(ContentTitle)
                .setContentText(ContentText)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        notificationManager.notify(0, notification);
    }
}
