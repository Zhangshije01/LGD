package com.lgd.lgdthesis.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by 蜗牛 on 2017-05-06.
 */

public class FindCircleBean extends BmobObject{
    private String avdator;//用户头像
    private String user_name;//用户昵称
    private String time;//用户发表文章时间
    private String article_name;//文章名字
    private String article_type;//文章类型
    private String hasReaded;//文章被阅读数
    private String hasComment;//文章被评论数
    private String hasFlad;//文章被喜欢数
    private String hasZan;//文章被赞数
    private String details;//文章内容

    public FindCircleBean() {
    }

    public FindCircleBean(String avdator, String user_name, String time, String article_name, String article_type, String hasReaded, String hasComment, String hasFlad, String hasZan, String details) {
        this.avdator = avdator;
        this.user_name = user_name;
        this.time = time;
        this.article_name = article_name;
        this.article_type = article_type;
        this.hasReaded = hasReaded;
        this.hasComment = hasComment;
        this.hasFlad = hasFlad;
        this.hasZan = hasZan;
        this.details = details;
    }

    public void setAvdator(String avdator) {
        this.avdator = avdator;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setArticle_name(String article_name) {
        this.article_name = article_name;
    }

    public void setArticle_type(String article_type) {
        this.article_type = article_type;
    }

    public void setHasReaded(String hasReaded) {
        this.hasReaded = hasReaded;
    }

    public void setHasComment(String hasComment) {
        this.hasComment = hasComment;
    }

    public void setHasFlad(String hasFlad) {
        this.hasFlad = hasFlad;
    }

    public void setHasZan(String hasZan) {
        this.hasZan = hasZan;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAvdator() {
        return avdator;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getTime() {
        return time;
    }

    public String getArticle_name() {
        return article_name;
    }

    public String getArticle_type() {
        return article_type;
    }

    public String getHasReaded() {
        return hasReaded;
    }

    public String getHasComment() {
        return hasComment;
    }

    public String getHasFlad() {
        return hasFlad;
    }

    public String getHasZan() {
        return hasZan;
    }

    public String getDetails() {
        return details;
    }
}
