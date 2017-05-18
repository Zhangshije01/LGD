package com.lgd.lgdthesis.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.app.LGDApplication;
import com.lgd.lgdthesis.base.BasesActivity;
import com.lgd.lgdthesis.bean.FindCircleBean;
import com.lgd.lgdthesis.bean.UserBean;
import com.lgd.lgdthesis.cache.LGDSharedprefrence;
import com.lgd.lgdthesis.databinding.ActivityLoginRegistBinding;
import com.lgd.lgdthesis.mvp.contract.HomeContract;
import com.lgd.lgdthesis.mvp.precenter.HomePresenter;
import com.lgd.lgdthesis.utils.LogUtils;
import com.lgd.lgdthesis.utils.ToastUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class LoginRegistActivity extends BasesActivity implements HomeContract.MvpView {

    ActivityLoginRegistBinding mBinding;
    private UserBean userBean;
    private HomeContract.Presenter mPresenter;

    public static void start(Context context, UserBean userBean) {
        Intent intent = new Intent(context, LoginRegistActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("userbean", userBean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginRegistActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login_regist);
        userBean = (UserBean) getIntent().getSerializableExtra("userbean");
        mPresenter = new HomePresenter(this);
        initData();
        mBinding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistActivity.start(LoginRegistActivity.this);
            }
        });

        mBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mBinding.etUsername.getText().toString();
                String userPassword = mBinding.etPsw.getText().toString();
                if(TextUtils.isEmpty(userName)||TextUtils.isEmpty(userPassword)){
                    ToastUtils.show("用户名、密码不能为空");
                    return;
                }
//                mPresenter.initLogin(LoginRegistActivity.this, userName, userPassword);
                initLogin(userName, userPassword);
            }
        });
    }
    public void initLogin(final String userName, final String userPassword){
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
                            LGDSharedprefrence.setUserAccount(userName);
                            LGDSharedprefrence.setUserPassword(password);
                            LGDSharedprefrence.setUserObjectId(userBean.getObjectId());
                            LGDSharedprefrence.setUserInstallId(userBean.getInstallId());
                            LGDApplication.getInstance().setUserBean(userBean);
                            UserDetailsActivity.start(LoginRegistActivity.this);
                            finish();
                        }
                    }
                }else{
                    ToastUtils.show(e.getMessage());
                }
            }
        });
    }


    public void initData() {
        if (userBean != null) {
            LogUtils.d(userBean.getUserName() + "--" + userBean.getUserPassword());
            mBinding.etUsername.setText(userBean.getUserAccount());
            mBinding.etPsw.setText(userBean.getUserPassword());
        }
    }

    @Override
    public void attachPresenter(HomeContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public Context getContext() {
        return this;
    }

}
