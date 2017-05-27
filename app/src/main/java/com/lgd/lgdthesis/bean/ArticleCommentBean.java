package com.lgd.lgdthesis.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 蜗牛 on 2017-05-26.
 */

public class ArticleCommentBean extends BmobObject{
    private String curr_user_name;
    private String fri_user_name;
    private String article_name;
    private String comment;
    private String time;

    public ArticleCommentBean() {
    }

    public String getCurr_user_name() {
        return curr_user_name;
    }

    public void setCurr_user_name(String curr_user_name) {
        this.curr_user_name = curr_user_name;
    }

    public String getFri_user_name() {
        return fri_user_name;
    }

    public void setFri_user_name(String fri_user_name) {
        this.fri_user_name = fri_user_name;
    }

    public String getArticle_name() {
        return article_name;
    }

    public void setArticle_name(String article_name) {
        this.article_name = article_name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
