package com.lgd.lgdthesis.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.databinding.ActivityUserDetailsBinding;

public class UserDetailsActivity extends AppCompatActivity {

    ActivityUserDetailsBinding mBinding;
    public static void start(Context context){
        Intent intent = new Intent(context,UserDetailsActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_user_details);
    }
}
