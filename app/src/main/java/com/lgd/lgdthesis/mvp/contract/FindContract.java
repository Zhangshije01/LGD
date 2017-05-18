package com.lgd.lgdthesis.mvp.contract;

import android.content.Context;

import com.lgd.lgdthesis.bean.FindCircleBean;
import com.lgd.lgdthesis.mvp.precenter.BasePresenter;
import com.lgd.lgdthesis.mvp.view.BaseMvpView;

import java.util.List;

/**
 * Created by 蜗牛 on 2017-05-16.
 */

public interface FindContract {

    interface Presenter extends BasePresenter {
        void showName();
        void queryArticle(int i);
    }
    interface FindView extends BaseMvpView<FindContract.Presenter>{
        void updateArticle(List<FindCircleBean> findCircleBeen);
    }
}
