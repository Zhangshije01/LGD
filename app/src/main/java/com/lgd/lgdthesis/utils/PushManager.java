package com.lgd.lgdthesis.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.app.LGDApplication;

import java.io.File;

/**
 * Created by 蜗牛 on 2017-05-17.
 */

public class PushManager {
    public static void sendNotification(Context context, String ticker, String title, String content) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setTicker(ticker);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.icon_push_notifi);

        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_VIBRATE;
        manager.notify(100, notification);

    }

}
