package com.lgd.lgdthesis.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.lgd.lgdthesis.app.LGDAppUtils;
import com.lgd.lgdthesis.app.LGDApplication;

/**
 * Created by 蜗牛 on 2017-05-15.
 */

public class LGDSharedprefrence implements LGDAppUtils.PreferenceKeys{
    private static final String PREFS_FILE = "lgd_thesis_prefs";
    private static SharedPreferences sPrefs;
    private static SharedPreferences initSharedPreferences() {
        if (sPrefs == null) {
            sPrefs = LGDApplication.getmContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        }
        return sPrefs;
    }

    public static void setUserAccount(String userAccount){
        SharedPreferences.Editor editor = initSharedPreferences().edit();
        editor.putString(USER_ACCOUNT,userAccount);
        editor.apply();
    }
    public static String getUserAccount(){
        SharedPreferences sharedPreferences = initSharedPreferences();
        return sharedPreferences.getString(USER_ACCOUNT,"");
    }

    public static void setUserPassword(String userAccount){
        SharedPreferences.Editor editor = initSharedPreferences().edit();
        editor.putString(USER_PASWORD,userAccount);
        editor.apply();
    }
    public static String getUserPassword(){
        SharedPreferences sharedPreferences = initSharedPreferences();
        return sharedPreferences.getString(USER_PASWORD,"");
    }

    public static void setUserObjectId(String userAccount){
        SharedPreferences.Editor editor = initSharedPreferences().edit();
        editor.putString(USER_OBJECTID,userAccount);
        editor.apply();
    }
    public static String getUserObjectId(){
        SharedPreferences sharedPreferences = initSharedPreferences();
        return sharedPreferences.getString(USER_OBJECTID,"");
    }
}
