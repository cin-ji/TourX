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

public class ExcursionReceiver extends BroadcastReceiver {
    public static final String CHANNEL_ID = "excursion_channel";
    public static final String KEY_EXCURSION_TITLE = "excursion_title";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("VacationReceiver", "onReceive called");

        String excursionTitle = intent.getStringExtra(KEY_EXCURSION_TITLE);

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Channel 1",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        Intent contentIntent = new Intent(context, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, 0, contentIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Excursion Notification")
                .setContentText(excursionTitle)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(contentPendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true);

        Log.d("ExcursionReceiver", "Notification created");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }
}