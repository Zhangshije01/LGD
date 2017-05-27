package com.lgd.lgdthesis.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 蜗牛 on 2017-05-26.
 */

public class CommentBean extends BmobObject {
    private String article_name;
    private Boolean isCollect;
    private String cru_userName;
    private String friend_name;

    public String getArticle_name() {
        return article_name;
    }

    public void setArticle_name(String article_name) {
        this.article_name = article_name;
    }

    public Boolean getCollect() {
        return isCollect;
    }

    public void setCollect(Boolean collect) {
        isCollect = collect;
    }

    public String getCru_userName() {
        return cru_userName;
    }

    public void setCru_userName(String cru_userName) {
        this.cru_userName = cru_userName;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }
}
