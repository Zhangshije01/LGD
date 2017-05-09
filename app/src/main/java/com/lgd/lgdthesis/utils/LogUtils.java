package com.lgd.lgdthesis.utils;

import android.util.Log;

/**
 * Created by 蜗牛 on 2017-05-05.
 */

public class LogUtils {
    private static boolean isDebug = true;
    private static final String TAG = "zsj tag";
    public static void d(CharSequence str){
        if(isDebug){
            Log.d(TAG,str.toString());
        }
    }
}
