package br.com.fiomaravilhabarbearia.fio_maravilha;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.mixpanel.android.mpmetrics.GCMReceiver;


/**
 * Created by fraps on 10/15/16.
 */

public class CustomMixpanelGCMReceiver extends GCMReceiver {

    public static String NOTIFICATION_ID = "notification_id";
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        if (notification != null) {
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            int id = intent.getIntExtra(NOTIFICATION_ID, 0);
            notificationManager.notify(id, notification);
        } else {
            intent.putExtra("mp_icnm", "ic_stat_notification_logo");
            super.onReceive(context, intent);
        }
    }
}