package com.lgd.lgdthesis.activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.base.BasesActivity;
import com.lgd.lgdthesis.databinding.ActivityMainBinding;
import com.lgd.lgdthesis.fragment.FindFragment;
import com.lgd.lgdthesis.fragment.MainFragment;
import com.lgd.lgdthesis.fragment.MessageFragment;
import com.lgd.lgdthesis.fragment.UserFragment;
import com.lgd.lgdthesis.mvp.contract.HomeContract;
import com.lgd.lgdthesis.mvp.precenter.HomePresenter;

public class MainActivity extends BasesActivity implements HomeContract.MvpView{
    ActivityMainBinding mBinding;
    private Button[] mTabs;
    private MainFragment mainFragment;
    private FindFragment findFragment;
    private UserFragment userFragment;
    private MessageFragment messageFragment;
    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;
    private HomeContract.Presenter homePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        homePresenter = new HomePresenter(this);
        initView();

//        final UMImage thumb =  new UMImage(this, R.drawable.umeng_socialize_back_icon);
//        mBinding.btShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new ShareAction(MainActivity.this).withMedia(thumb)
//                        .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN)
//                        .setCallback(umShareListener).open();
//            }
//        });
//        if(Build.VERSION.SDK_INT>=23){
//            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.CALL_PHONE,
//                    Manifest.permission.READ_LOGS,
//                    Manifest.permission.READ_PHONE_STATE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.SET_DEBUG_APP,
//                    Manifest.permission.SYSTEM_ALERT_WINDOW,
//                    Manifest.permission.GET_ACCOUNTS,
//                    Manifest.permission.WRITE_APN_SETTINGS};
//            ActivityCompat.requestPermissions(this,mPermissionList,123);
//        }

    }
    public void initView(){
        mTabs = new Button[4];
        mTabs[0] = mBinding.btnMainBottomMain;
        mTabs[1] = mBinding.btnMainBottomFind;
        mTabs[2] = mBinding.btnMainBottomMessage;
        mTabs[3] = mBinding.btnMainBottomUser;
        mTabs[0].setSelected(true);
        initTab();
    }
    private void initTab(){
        mainFragment = new MainFragment();
        findFragment = new FindFragment();
        messageFragment = new MessageFragment();
        userFragment=new UserFragment();
        fragments = new Fragment[] {mainFragment, findFragment,messageFragment,userFragment};
        getSupportFragmentManager().beginTransaction()
                .add(R.id.ll_main_contaner, mainFragment).
                add(R.id.ll_main_contaner, findFragment)
                .add(R.id.ll_main_contaner,messageFragment)
                .add(R.id.ll_main_contaner, userFragment)
                .hide(findFragment).hide(userFragment).hide(messageFragment)
                .show(mainFragment).commit();
    }
    public void onTabSelect(View view){
        switch (view.getId()) {
            case R.id.btn_main_bottom_main:
                index = 0;
                break;
            case R.id.btn_main_bottom_find:
                index = 1;
                break;
            case R.id.btn_main_bottom_message:
                index = 2;
                break;
            case R.id.btn_main_bottom_user:
                index = 3;
                break;
        }
        onTabIndex(index);
    }

    private void onTabIndex(int index){
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.ll_main_contaner, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }

    @Override
    public void attachPresenter(HomeContract.Presenter presenter) {
        this.homePresenter = presenter;
    }

    @Override
    public Context getContext() {
        return this;
    }
}
