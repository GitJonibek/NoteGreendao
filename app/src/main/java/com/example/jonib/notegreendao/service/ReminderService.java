package com.example.jonib.notegreendao.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.jonib.notegreendao.MainActivity;
import com.example.jonib.notegreendao.R;

public class ReminderService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        String state = intent.getExtras().getString("extra");

        assert state != null;


        Intent main_intent = new Intent(this, MainActivity.class);
        main_intent.setAction(Intent.ACTION_SHUTDOWN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, main_intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "id")
                .setSmallIcon(R.mipmap.notification_opened_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.notification_icon))
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, mBuilder.build());

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
