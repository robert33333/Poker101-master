package com.example.poker101;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.
        // For our recurring task, we'll just display a message
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "1")
                        .setSmallIcon(R.drawable.heart)
                        .setContentTitle("Daily money")
                        .setContentText("Click the button for the daily money (500)");
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(9011, mBuilder.build());
    }
}
