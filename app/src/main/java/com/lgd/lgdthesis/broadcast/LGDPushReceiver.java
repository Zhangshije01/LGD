package com.lgd.lgdthesis.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lgd.lgdthesis.app.LGDApplication;
import com.lgd.lgdthesis.utils.LogUtils;
import com.lgd.lgdthesis.utils.PushManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.push.PushConstants;

/**
 * Created by 蜗牛 on 2017-05-17.
 */

public class LGDPushReceiver extends BroadcastReceiver {
    private static List<EventListener> list = new ArrayList<EventListener>();

    public static void regist(EventListener listener) {
        list.add(listener);
    }

    public static void unRegist(EventListener listener) {
        list.remove(listener);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d("bmob" + intent.getAction());
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            Log.d("bmob", "客户端收到推送内容：" + intent.getStringExtra("msg"));
            try {
                String message = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
                JSONObject jsonObj = new JSONObject(message);
                if (jsonObj.has("tag")) {
                    String tag = jsonObj.getString("tag");
                    if (tag.equals("push")) {
                        String obj = jsonObj.getString("obj");
                        PushManager.sendNotification(LGDApplication.getInstance(), "", "来自lgdThesis的推送", obj);
                    }
                    if ("new".equals(tag)) {
                        LogUtils.d("接受到了");
                        if (list.size() > 0) {
                            for (EventListener listener : list) {
                                listener.onNewPost();
                            }

                        }
                        String user = jsonObj.getString("user");
                        PushManager.sendNotification(LGDApplication.getInstance(), "", "来自lgdThesis的推送", user + "发了一个新的文章");
                    }
                    if ("one".equals(tag)) {
                        LogUtils.d("接受到了");
                        if (list.size() > 0) {
                            for (EventListener listener : list) {
                                listener.onAtOne();
                            }
                        }
                        String user = jsonObj.getString("user");
                        PushManager.sendNotification(LGDApplication.getInstance(), "", "来自lgdThesis的推送", user + "发了一个新的文章");
                    }

                    if ("time".equals(tag)) {
                        LogUtils.d("接受到了");
                        if (list.size() > 0) {
                            for (EventListener listener : list) {
                                listener.onAtOne();
                            }
                        }
                        String user = jsonObj.getString("obj");
                        PushManager.sendNotification(LGDApplication.getInstance(), "", "来自lgdThesis的推送", user);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public interface EventListener {
        void onNewPost();

        void onAtOne();
    }
}
