package com.lgd.lgdthesis.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 蜗牛 on 2017-05-06.
 */

public class MainFindBean extends BmobObject{
    private UserBean userBean;//用户信息
    private String article_name;//文章名字
    private String user_name;
    private String user_avator;
    public MainFindBean() {
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public String getArticle_name() {
        return article_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_avator() {
        return user_avator;
    }

    public void setUser_avator(String user_avator) {
        this.user_avator = user_avator;
    }

    public void setArticle_name(String article_name) {
        this.article_name = article_name;
    }

    @Override
    public String toString() {
        return "MainFindBean{" +
                "userBean=" + userBean +
                ", article_name='" + article_name + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_avator='" + user_avator + '\'' +
                '}';
    }
}
