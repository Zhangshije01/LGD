package com.lgd.lgdthesis.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 蜗牛 on 2017-05-07.
 */

public class UserBean extends BmobObject{
    private String userAnvator;//用户头像
    private String userName;//用户昵称
    private String homePage;//用户主页
    private String userDetail;//用户主页
    private String userAccount;//用户账号
    private String userPassword;//用户密码
    private String installId;//手机设备号

    public UserBean() {
    }

    public UserBean(String userAnvator, String userName, String homePage, String userDetail, String userAccount, String userPassword) {
        this.userAnvator = userAnvator;
        this.userName = userName;
        this.homePage = homePage;
        this.userDetail = userDetail;
        this.userAccount = userAccount;
        this.userPassword = userPassword;
    }

    public String getUserAnvator() {
        return userAnvator;
    }

    public void setUserAnvator(String userAnvator) {
        this.userAnvator = userAnvator;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(String userDetail) {
        this.userDetail = userDetail;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setInstallId(String installId) {
        this.installId = installId;
    }

    public String getInstallId() {
        return installId;
    }
}
