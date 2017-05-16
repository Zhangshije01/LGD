package com.lgd.lgdthesis.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.lgd.lgdthesis.bean.UserBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.socialize.Config;
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
    private static Context mContext;
    private UserBean userBean;
    private String url;
    private boolean isFirst;
    private String filepath;
    @Override
    public void onCreate() {
        super.onCreate();
        lgdApplication = this;
        mContext = getApplicationContext();
        mHandler = new Handler();
        UMShareAPI.get(this);
        //bmob 账号是  微信  项目是LGDTheists
        Bmob.initialize(this,"71f596145338c007bf6ba1b5edca5d2f");
        SMSSDK.initSDK(this,"1da2d30d3e5fe","cd8df2b3736165eee3a0d51fbf9e6bf3");
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        Config.DEBUG=true;
        isFirst = true;
        Config.isNeedAuth = true;
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

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public static Context getmContext() {
        return mContext;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public boolean isFirst() {
        return isFirst;
    }
    public void setFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }
    public String getFilepath() {
        return filepath;
    }
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
