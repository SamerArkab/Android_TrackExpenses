package com.example.trackexpenses;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class ExpenseReminderReceiver extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ExpenseReminderService.CHANNEL_ID)
                .setSmallIcon(R.mipmap.trackexpenses_icon)
                .setContentTitle("Expense Reminder")
                .setContentText("Don't forget to add your expenses today.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(ExpenseReminderService.NOTIFICATION_ID, builder.build());
    }
}
