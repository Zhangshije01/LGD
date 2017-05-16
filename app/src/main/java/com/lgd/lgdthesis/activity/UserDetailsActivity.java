package com.lgd.lgdthesis.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.adapter.UserDetailViewPagerAdapter;
import com.lgd.lgdthesis.app.LGDApplication;
import com.lgd.lgdthesis.bean.UserBean;
import com.lgd.lgdthesis.databinding.ActivityUserDetailsBinding;
import com.lgd.lgdthesis.fragment.detailFragment.UserDetailArticleFragment;
import com.lgd.lgdthesis.fragment.detailFragment.UserDetailMoreFragment;
import com.lgd.lgdthesis.fragment.detailFragment.UserDetaildynamicFragment;
import com.lgd.lgdthesis.utils.LogUtils;

import java.util.List;

public class UserDetailsActivity extends AppCompatActivity {

    ActivityUserDetailsBinding mBinding;
    private UserDetaildynamicFragment dynamicFragment;
    private UserDetailArticleFragment articlFragment;
    private UserDetailMoreFragment moreFragment;
    private UserBean userBean;
    public static void start(Context context){
        Intent intent = new Intent(context,UserDetailsActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_user_details);
        userBean = LGDApplication.getInstance().getUserBean();
        mBinding.tvUserDeatilLoginRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRegistActivity.start(UserDetailsActivity.this);
            }
        });
        initFragment();
        setViewPagerClick();
        mBinding.tvUserEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserEditActivity.start(UserDetailsActivity.this);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    public void initView(){
        if(userBean == null){
            mBinding.tvUserEdit.setVisibility(View.INVISIBLE);
            mBinding.tvUserDeatilLoginRegist.setVisibility(View.VISIBLE);
        }else{
            mBinding.tvUserEdit.setVisibility(View.VISIBLE);
            mBinding.tvUserDeatilLoginRegist.setVisibility(View.INVISIBLE);
        }
    }
    public void initFragment(){
        dynamicFragment = new UserDetaildynamicFragment();
        articlFragment = new UserDetailArticleFragment();
        moreFragment = new UserDetailMoreFragment();

        UserDetailViewPagerAdapter adapter_viewpager = new UserDetailViewPagerAdapter(getSupportFragmentManager());
        adapter_viewpager.addFragment(dynamicFragment);
        adapter_viewpager.addFragment(articlFragment);
        adapter_viewpager.addFragment(moreFragment);
        mBinding.viewpagerUserdetail.setAdapter(adapter_viewpager);
        mBinding.viewpagerUserdetail.setCurrentItem(1);
    }
    public void setViewPagerClick(){
        mBinding.radioGroupUserDetail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int radioId = mBinding.radioGroupUserDetail.indexOfChild(group.findViewById(checkedId));
                if(mBinding.viewpagerUserdetail!=null){
                    mBinding.viewpagerUserdetail.setCurrentItem(radioId);
                }
            }
        });
    }
}
