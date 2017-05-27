package com.lgd.lgdthesis.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 蜗牛 on 2017-05-24.
 */

public class FriendBean extends BmobObject{
    private String phone;
    private String anvator;
    private String name;

    public FriendBean(String anvator, String name) {
        this.anvator = anvator;
        this.name = name;
    }

    public FriendBean() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAnvator() {
        return anvator;
    }

    public void setAnvator(String anvator) {
        this.anvator = anvator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
