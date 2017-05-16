package com.lgd.lgdthesis.mvp.precenter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.lgd.lgdthesis.activity.UserDetailsActivity;
import com.lgd.lgdthesis.app.LGDApplication;
import com.lgd.lgdthesis.bean.UserBean;
import com.lgd.lgdthesis.mvp.contract.HomeContract;
import com.lgd.lgdthesis.utils.LogUtils;
import com.lgd.lgdthesis.utils.ToastUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by admin on 17/4/12.
 */
public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.MvpView mvpView;
    private static final String TAG = "HomePresenter zsj TAG";

    public HomePresenter(HomeContract.MvpView mvpView) {
        this.mvpView = mvpView;
        this.mvpView.attachPresenter(this);
    }

    @Override
    public void subscribe() {
        Log.d(TAG, "subscribe: ");
    }

    @Override
    public void unSubscribe() {
    }

    @Override
    public void showName() {
        ToastUtils.show("aaa");
    }

    @Override
    public void initLogin(final Context context, final String userName, final String userPassword) {
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
                            ToastUtils.show("登录成功");
                            UserBean userBean = list.get(0);
                            LGDApplication.getInstance().setUserBean(userBean);
                            UserDetailsActivity.start(context);

                        }
                    }
                }else{
                    ToastUtils.show(e.getMessage());
                }
            }
        });
    }
}
