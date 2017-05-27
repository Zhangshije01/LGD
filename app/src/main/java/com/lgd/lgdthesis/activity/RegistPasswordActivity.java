package com.lgd.lgdthesis.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.bean.UserBean;
import com.lgd.lgdthesis.databinding.ActivityRegistPasswordBinding;
import com.lgd.lgdthesis.utils.ToastUtils;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegistPasswordActivity extends AppCompatActivity {
    ActivityRegistPasswordBinding mBinding;
    private String phonenum;
    public static void start(Context context,String phonenum) {
        Intent intent = new Intent(context, RegistPasswordActivity.class);
        intent.putExtra("phonenum",phonenum);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_regist_password);
        phonenum = getIntent().getStringExtra("phonenum");

        mBinding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String password = mBinding.etPassword.getText().toString();

                if(password.length()==0|| TextUtils.isEmpty(password)){
                    ToastUtils.show("请输入密码");
                    return;
                }
                final UserBean userBean = new UserBean();
                userBean.setUserAccount(phonenum);
                userBean.setUserPassword(password);
                userBean.setInstallId(BmobInstallation.getInstallationId(RegistPasswordActivity.this));
                userBean.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e == null){
                            ToastUtils.show("注册成功");

                            LoginRegistActivity.start(RegistPasswordActivity.this,userBean);

                            finish();
                        }else {
                            ToastUtils.show("注册失败："+e.getMessage());
                            return;
                        }
                    }
                });
            }
        });
        mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
