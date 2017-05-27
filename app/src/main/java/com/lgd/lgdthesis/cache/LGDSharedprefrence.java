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
    public static void setUserName(String userName){
        SharedPreferences.Editor editor = initSharedPreferences().edit();
        editor.putString(USER_NAME,userName);
        editor.apply();
    }
    public static String getUserName(){
        SharedPreferences sharedPreferences = initSharedPreferences();
        return sharedPreferences.getString(USER_NAME,"");
    }
    public static void setUserAvator(String userName){
        SharedPreferences.Editor editor = initSharedPreferences().edit();
        editor.putString(USER_AVATOR,userName);
        editor.apply();
    }
    public static String getUserAvator(){
        SharedPreferences sharedPreferences = initSharedPreferences();
        return sharedPreferences.getString(USER_AVATOR,"");
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

    public static void setUserInstallId(String userInstallId){
        SharedPreferences.Editor editor = initSharedPreferences().edit();
        editor.putString(USER_INSTALLID,userInstallId);
        editor.apply();
    }
    public static String getUserInstallId(){
        SharedPreferences sharedPreferences = initSharedPreferences();
        return sharedPreferences.getString(USER_INSTALLID,"");
    }

    public static void setAllreadyPermission(){
        SharedPreferences.Editor editor = initSharedPreferences().edit();
        editor.putBoolean(ALLREADY_PERMISSION,true);
        editor.apply();
    }
    public static boolean getAllreadyPermission(){
        SharedPreferences sharedPreferences = initSharedPreferences();
        return sharedPreferences.getBoolean(ALLREADY_PERMISSION,false);
    }

    public static void setAllreadyScanFile(){
        SharedPreferences.Editor editor = initSharedPreferences().edit();
        editor.putBoolean(ALREADY_SCAN_FILE,true);
        editor.apply();
    }
    public static boolean getAllreadyScanFile(){
        SharedPreferences sharedPreferences = initSharedPreferences();
        return sharedPreferences.getBoolean(ALREADY_SCAN_FILE,false);
    }
}
