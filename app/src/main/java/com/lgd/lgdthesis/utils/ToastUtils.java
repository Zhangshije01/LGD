package com.lgd.lgdthesis.utils;

import android.widget.Toast;

import com.lgd.lgdthesis.app.LGDApplication;

/**
 * Created by 蜗牛 on 2017-05-05.
 */

public class ToastUtils {
    private static Toast showToast = null;
    public static void show(CharSequence charSequence){
        show(charSequence , -1);
    }
    public static void show(final CharSequence charSequence, final int gravity){
        LGDApplication.getInstance().getUIHandler().post(new Runnable() {
            @Override
            public void run() {
                int duration ;
                if(charSequence.length() >= 15){
                    duration = Toast.LENGTH_LONG;
                }else{
                    duration  = Toast.LENGTH_SHORT;
                }
                showToast = Toast.makeText(LGDApplication.getInstance(),charSequence,duration);
                if(gravity > 0){
                    showToast.setGravity(gravity , 0 , 0);
                }
                showToast.show();
            }
        });
    }
}
