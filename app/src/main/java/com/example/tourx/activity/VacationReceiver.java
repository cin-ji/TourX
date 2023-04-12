package com.example.tourx.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.tourx.MainActivity;
import com.example.tourx.R;

public class VacationReceiver extends BroadcastReceiver {
    public static final String CHANNEL_ID = "excursion_channel";
    public static final String KEY_VACATION_TITLE = "vacation_title";
    public static final String KEY_IS_STARTING = "is_starting";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("VacationReceiver", "onReceive called");

        String vacationTitle = intent.getStringExtra(KEY_VACATION_TITLE);
        boolean isStarting = intent.getBooleanExtra(KEY_IS_STARTING, false);

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Channel 2",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        // Create a content intent to open the app when the notification is clicked
        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, 0, contentIntent, PendingIntent.FLAG_IMMUTABLE);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(isStarting ? "Vacation is starting!" : "Vacation is ending!")
                .setContentText(vacationTitle)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(contentPendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true);


        Log.d("ExcursionReceiver", "Notification created");
        // Show the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}