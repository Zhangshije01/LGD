package com.lgd.lgdthesis.mvp.precenter;

import com.lgd.lgdthesis.bean.FindCircleBean;
import com.lgd.lgdthesis.mvp.contract.FindContract;
import com.lgd.lgdthesis.utils.LogUtils;
import com.lgd.lgdthesis.utils.ToastUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 蜗牛 on 2017-05-16.
 */

public class FindPresenter implements FindContract.Presenter{

    private FindContract.FindView findView;

    public FindPresenter(FindContract.FindView findView) {
        this.findView = findView;
        this.findView.attachPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void showName() {

    }

    @Override
    public void queryArticle(int i) {
        BmobQuery<FindCircleBean> query = new BmobQuery<>();
        query.setLimit(10).setSkip(10*(i-1)).order("-createdAt")
                .findObjects(new FindListener<FindCircleBean>() {
                    @Override
                    public void done(List<FindCircleBean> list, BmobException e) {
                        if(e == null){
                            findView.updateArticle(list);
                        }else{
                            ToastUtils.show(e.getMessage());
                            return;
                        }
                    }
                });
    }
}
