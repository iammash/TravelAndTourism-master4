package com.example.android.travelandtourism;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.example.android.travelandtourism.Activities.HomeActivity;
import com.example.android.travelandtourism.Activities.MyHotelReservations;

import java.util.Date;

/**
 * Created by dania on 10/6/2017.
 */

public class AlertReceiver extends BroadcastReceiver {
    String Title ="";
    String content_msg = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        Title = intent.getStringExtra("title");
        content_msg = intent.getStringExtra("msg");
        int Code = Integer.parseInt( intent.getStringExtra("code") );

        Intent intent2 = new Intent(context, HomeActivity.class);
        intent2.putExtra("title",Title);
        intent2.putExtra("msg", content_msg);

        creatNotification(context, Title, content_msg, intent2, Code);

    }

    public void creatNotification(Context context, String title, String msg, Intent intent_dialog, int code) {

        PendingIntent noti = PendingIntent.getActivity(context ,code, intent_dialog,0);


        NotificationManager nmanager2 ;
        NotificationCompat.Builder nbuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(R.drawable.notify)
                .setContentIntent(noti);

        nmanager2 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nmanager2.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), nbuilder.build());
        //noti_id2++;
    }
}
