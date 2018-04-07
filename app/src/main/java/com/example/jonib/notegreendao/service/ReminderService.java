package com.example.jonib.notegreendao.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationManagerCompat;

import com.example.jonib.notegreendao.MainActivity;
import com.example.jonib.notegreendao.MainViewActivity;
import com.example.jonib.notegreendao.R;

import java.util.Locale;


public class ReminderService extends IntentService {

    private static final int NOTIFICATION_ID = 3;

    public ReminderService() {
        super("ReminderService");
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent notifyIntent = new Intent(this, MainViewActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this)
                .setContentTitle("My Title!")
                .setContentText("This is the Body")
                .setSmallIcon(R.mipmap.notification_opened_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.launcher_icon))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);

        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
    }
}
