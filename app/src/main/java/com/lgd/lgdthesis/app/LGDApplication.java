package com.lgd.lgdthesis.app;

import android.app.Application;
import android.os.Handler;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import cn.bmob.v3.Bmob;
import cn.smssdk.SMSSDK;

/**
 * Created by 蜗牛 on 2017-05-04.
 */

public class LGDApplication extends Application{
    private static LGDApplication lgdApplication;
    private static Handler mHandler;
    @Override
    public void onCreate() {
        super.onCreate();
        lgdApplication = this;
        mHandler = new Handler();
        UMShareAPI.get(this);
        //bmob 账号是15931431006
        Bmob.initialize(this,"2da37c410a7a2956bc69acd687a191dd");
        SMSSDK.initSDK(this,"1da2d30d3e5fe","cd8df2b3736165eee3a0d51fbf9e6bf3");

        com.umeng.socialize.Config.DEBUG=true;
        com.umeng.socialize.Config.isNeedAuth = true;
        PlatformConfig.setWeixin("wxf092b6b67170bbb9", "26d9811cc1518d5f9835ec296d3c01b1");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
    }
    public static LGDApplication getInstance(){
        return lgdApplication;
    }
    public static Handler getUIHandler(){
        return mHandler;
    }
}
