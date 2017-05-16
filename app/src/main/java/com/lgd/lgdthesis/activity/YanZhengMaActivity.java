package com.lgd.lgdthesis.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.databinding.ActivityYanZhengMaBinding;
import com.lgd.lgdthesis.utils.LogUtils;
import com.lgd.lgdthesis.utils.ToastUtils;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class YanZhengMaActivity extends AppCompatActivity {

    ActivityYanZhengMaBinding mBinding;
    private EventHandler eh;
    private String regist_phone;
    public static void start(Context context,String phonenum) {
        Intent intent = new Intent(context, YanZhengMaActivity.class);
        intent.putExtra("phoneNum",phonenum);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_yan_zheng_ma);
        regist_phone = getIntent().getStringExtra("phoneNum");
        mBinding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String yanzhengma = mBinding.etYanzhengma.getText().toString();
                if (TextUtils.isEmpty(yanzhengma) || yanzhengma.length() == 0) {
                    ToastUtils.show("验证码为空");
                    return;
                }
                SMSSDK.submitVerificationCode("86", regist_phone , yanzhengma);
                LogUtils.d("call: "+regist_phone+yanzhengma);
                eh=new EventHandler(){
                    @Override
                    public void afterEvent(int event, int result, Object data) {

                        if (result == SMSSDK.RESULT_COMPLETE) {
                            //回调完成
                            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                                //提交验证码成功
                                ToastUtils.show("短信验证成功");
                                LogUtils.d("afterEvent: "+regist_phone+yanzhengma);
                                RegistPasswordActivity.start(YanZhengMaActivity.this,regist_phone);
                            }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                                //获取验证码成功
                                LogUtils.d("afterEvent: 2"+regist_phone+yanzhengma);
                            }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                                //返回支持发送验证码的国家列表
                            }
                        }else{
                            ((Throwable)data).printStackTrace();
                        }
                    }
                };
                SMSSDK.registerEventHandler(eh);
            }
        });
    }
}
