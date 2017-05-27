package com.lgd.lgdthesis.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.base.BasesActivity;
import com.lgd.lgdthesis.databinding.ActivityWeChatBinding;

public class WeChatActivity extends BasesActivity {
    ActivityWeChatBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_we_chat);
    }
}
