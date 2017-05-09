package com.lgd.lgdthesis.mvp.contract;


import android.app.ProgressDialog;
import android.content.Context;

import com.lgd.lgdthesis.mvp.precenter.BasePresenter;
import com.lgd.lgdthesis.mvp.view.BaseMvpView;


/**
 * Created by admin on 17/4/12.
 */

public interface HomeContract {
    interface Presenter extends BasePresenter {
        void showName();
    }
    interface MvpView extends BaseMvpView<Presenter> {

    }
}
