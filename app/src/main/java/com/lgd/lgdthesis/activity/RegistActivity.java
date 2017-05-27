package com.lgd.lgdthesis.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.databinding.ActivityRegistBinding;
import com.lgd.lgdthesis.utils.ToastUtils;

import cn.smssdk.OnSendMessageHandler;

public class RegistActivity extends AppCompatActivity {
    ActivityRegistBinding mBinding;

    public static void start(Context context) {
        Intent intent = new Intent(context, RegistActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_regist);
        mBinding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phoneNum = mBinding.etPhoneNumber.getText().toString();
                if(phoneNum.length()<11){
                    ToastUtils.show("请输入11位有效手机号码");
                    return;
                }
                cn.smssdk.SMSSDK.getVerificationCode("86", phoneNum, new OnSendMessageHandler() {
                    @Override
                    public boolean onSendMessage(String s, String s1) {
                        Log.d("sendRAG",s+s1);
                        ToastUtils.show("ok");
                        YanZhengMaActivity.start(RegistActivity.this,phoneNum);
                        finish();
                        return false;
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
