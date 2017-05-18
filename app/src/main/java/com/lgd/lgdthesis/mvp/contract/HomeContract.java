package com.lgd.lgdthesis.mvp.contract;


import android.app.ProgressDialog;
import android.content.Context;
import android.widget.EditText;

import com.lgd.lgdthesis.bean.FindCircleBean;
import com.lgd.lgdthesis.bean.UserBean;
import com.lgd.lgdthesis.mvp.precenter.BasePresenter;
import com.lgd.lgdthesis.mvp.view.BaseMvpView;

import java.util.List;


/**
 * Created by admin on 17/4/12.
 */

public interface HomeContract {
    interface Presenter extends BasePresenter {
        void showName();
        void initLogin(Context context, String userName, String userPassword);
    }
    interface MvpView extends BaseMvpView<Presenter> {
    }
    interface FindView extends BaseMvpView<Presenter>{
        void updateArticle(List<FindCircleBean> findCircleBeen);
    }
}
