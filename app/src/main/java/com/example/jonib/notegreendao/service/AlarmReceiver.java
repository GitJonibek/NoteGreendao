package com.example.jonib.notegreendao.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String get_extra_string = intent.getExtras().getString("extra");

        Intent service = new Intent(context, ReminderService.class);
        service.putExtra("extra", get_extra_string);
        context.startService(service);
    }
}
