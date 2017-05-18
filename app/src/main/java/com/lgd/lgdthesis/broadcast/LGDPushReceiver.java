package com.lgd.lgdthesis.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lgd.lgdthesis.app.LGDApplication;
import com.lgd.lgdthesis.utils.DownloadManager;
import com.lgd.lgdthesis.utils.LogUtils;
import com.lgd.lgdthesis.utils.PushManager;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.push.PushConstants;

/**
 * Created by 蜗牛 on 2017-05-17.
 */

public class LGDPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d("bmob"+intent.getAction());
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            Log.d("bmob", "客户端收到推送内容："+intent.getStringExtra("msg"));
            try {
                String message = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
                JSONObject jsonObj =  new JSONObject(message);
                if(jsonObj.has("tag")) {
                    String tag = jsonObj.getString("tag");
                    if(tag.equals("push")){
                        String obj = jsonObj.getString("obj");
                        PushManager.sendNotification(LGDApplication.getInstance(),"","来自lgdThesis的推送",obj);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
