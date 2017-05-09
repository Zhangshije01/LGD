package com.lgd.lgdthesis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.umeng.socialize.media.WBShareCallBackActivity;

public class WBShareActivity extends WBShareCallBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wbshare);
    }
}
