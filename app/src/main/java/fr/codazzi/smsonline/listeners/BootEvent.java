package fr.codazzi.smsonline.listeners;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import fr.codazzi.smsonline.Tools;

public class BootEvent extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("EXIT")) {
            return;
        }
        /* START REVISION TIMER every 5 min */
        Tools.storeLog(context, "LISTENER : Phone booted");
        Intent myIntent = new Intent("fr.codazzi.smsonline.listeners.BootEvent");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 1000,
                300000, //120000, // 2 min in ms
                pendingIntent);

        /* START SYNC TIMER every 1 min */
        Intent myIntent2 = new Intent("fr.codazzi.smsonline.listeners.SyncEvent");
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(
                context,
                0,
                myIntent2,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager2 = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager2.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 1000,
                60000, //120000, // 1 min in ms
                pendingIntent2);
    }
}
