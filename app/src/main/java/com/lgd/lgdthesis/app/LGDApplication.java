package com.lgd.lgdthesis.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.lgd.lgdthesis.bean.UserBean;
import com.lgd.lgdthesis.cache.LGDSharedprefrence;
import com.lgd.lgdthesis.utils.LogUtils;
import com.lgd.lgdthesis.utils.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.List;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
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

    private String userName;
    private String userPassword;
    @Override
    public void onCreate() {
        super.onCreate();
        lgdApplication = this;
        mContext = getApplicationContext();
        mHandler = new Handler();
        UMShareAPI.get(this);
        //bmob 账号是  微信  项目是LGDTheists
        Bmob.initialize(this,"71f596145338c007bf6ba1b5edca5d2f");
        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation().save();
        // 启动推送服务
        BmobPush.startWork(this);
        SMSSDK.initSDK(this,"1da2d30d3e5fe","cd8df2b3736165eee3a0d51fbf9e6bf3");
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        Config.DEBUG=true;
        isFirst = true;
        Config.isNeedAuth = true;
        PlatformConfig.setWeixin("wxf092b6b67170bbb9", "26d9811cc1518d5f9835ec296d3c01b1");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");

        userName = LGDSharedprefrence.getUserAccount();
        userPassword = LGDSharedprefrence.getUserPassword();
        if(!TextUtils.isEmpty(userName)&&!TextUtils.isEmpty(userPassword)){
            LogUtils.d("sss");
            initLogin(userName,userPassword);
        }
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
    public void initLogin(final String userName, final String userPassword){
        BmobQuery<UserBean> query = new BmobQuery<>();
        query.addWhereEqualTo("userAccount",userName);
        query.findObjects(new FindListener<UserBean>() {
            @Override
            public void done(List<UserBean> list, BmobException e) {
                if(e == null){
                    String password = list.get(0).getUserPassword();
                    if(list !=null && list.size()>0){
                        if(userPassword.equals(password)){
                            //登录成功
                            UserBean userBean = list.get(0);
                            LGDApplication.getInstance().setUserBean(userBean);
                        }
                    }
                }else{
                    ToastUtils.show(e.getMessage());
                }
            }
        });
    }
}
