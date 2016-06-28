package fr.codazzi.smsonline.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import fr.codazzi.smsonline.controllers.Api;

/**
 * Created by azzod.fr on 28/06/2016.
 */
public class NLService extends NotificationListenerService {
    Context context;

    private void runApi() {
        Log.d("SYNC LOOP", "Listener");
        SharedPreferences settings = context.getSharedPreferences("swb_infos", 0);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getActiveNetworkInfo();

        if (        network == null
                || (settings.getBoolean("wifi_only", true) && network.getType() != ConnectivityManager.TYPE_WIFI)
                || !network.isConnectedOrConnecting()) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("error", 2);
            editor.apply();
            return;
        }

        new Api(context, settings).Run();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        runApi();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
