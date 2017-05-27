package com.lgd.lgdthesis.activity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.base.BasesActivity;
import com.lgd.lgdthesis.databinding.ActivityOptionBinding;

public class OptionActivity extends BasesActivity {

    ActivityOptionBinding mBinding;   // 控制输入键盘弹出
    private static Handler mControlHandler = new Handler();
    public static void start(Context context){
        Intent intent = new Intent(context,OptionActivity.class);
        context.startActivity(intent);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_option);

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        //切换软键盘的显示与隐藏
//        imm.toggleSoftInputFromWindow(mBinding.editUserFragmentOption.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
        //显示软键盘
        imm.showSoftInputFromInputMethod(mBinding.editUserFragmentOption.getWindowToken(), 0);

//        mControlHandler.postDelayed(new Runnable() {
//            @TargetApi(Build.VERSION_CODES.CUPCAKE)
//            @Override
//            public void run() {
//
//            }
//        }, 0);

//        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        if (imm.hideSoftInputFromWindow(mBinding.editUserFragmentOption.getWindowToken(), 0)) {
//            imm.showSoftInput(mBinding.editUserFragmentOption, 0);
//            //软键盘已弹出
//            LogUtils.d("已弹出");
//        } else {
//            //软键盘未弹出
//        }
        mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
