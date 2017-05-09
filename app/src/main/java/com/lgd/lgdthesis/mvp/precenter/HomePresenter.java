package com.lgd.lgdthesis.mvp.precenter;
import android.util.Log;
import com.lgd.lgdthesis.mvp.contract.HomeContract;
import com.lgd.lgdthesis.utils.ToastUtils;

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
}
