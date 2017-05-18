package com.lgd.lgdthesis.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.app.LGDApplication;
import com.lgd.lgdthesis.base.BasesActivity;
import com.lgd.lgdthesis.bean.UserBean;
import com.lgd.lgdthesis.cache.LGDSharedprefrence;
import com.lgd.lgdthesis.databinding.ActivityMainBinding;
import com.lgd.lgdthesis.fragment.FindFragment;
import com.lgd.lgdthesis.fragment.MainFragment;
import com.lgd.lgdthesis.fragment.MessageFragment;
import com.lgd.lgdthesis.fragment.UserFragment;
import com.lgd.lgdthesis.mvp.contract.HomeContract;
import com.lgd.lgdthesis.mvp.precenter.HomePresenter;
import com.lgd.lgdthesis.utils.LogUtils;
import com.lgd.lgdthesis.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends BasesActivity implements HomeContract.MvpView {
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
        toPushNotification();
    }

    public void initView() {
        mTabs = new Button[4];
        mTabs[0] = mBinding.btnMainBottomMain;
        mTabs[1] = mBinding.btnMainBottomFind;
        mTabs[2] = mBinding.btnMainBottomMessage;
        mTabs[3] = mBinding.btnMainBottomUser;
        mTabs[0].setSelected(true);
        initTab();
    }

    private void initTab() {
        mainFragment = new MainFragment();
        findFragment = new FindFragment();
        messageFragment = new MessageFragment();
        userFragment = new UserFragment();
        fragments = new Fragment[]{mainFragment, findFragment, messageFragment, userFragment};
        getSupportFragmentManager().beginTransaction()
                .add(R.id.ll_main_contaner, mainFragment).
                add(R.id.ll_main_contaner, findFragment)
                .add(R.id.ll_main_contaner, messageFragment)
                .add(R.id.ll_main_contaner, userFragment)
                .hide(findFragment).hide(userFragment).hide(messageFragment)
                .show(mainFragment).commit();
    }

    public void onTabSelect(View view) {
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

    private void onTabIndex(int index) {
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void toPushNotification() {
        //TODO 无法推送
        String installationId = LGDSharedprefrence.getUserInstallId();
        LogUtils.d("installationId"+installationId);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String curTime = sdf.format(new Date());
            boolean isPushTimeOne = isInTime("9:30-10:00", curTime);
            boolean isPushTimeTwo = isInTime("11:30-12:00", curTime);
            boolean isPushTimeThree = isInTime("17:00-21:00", curTime);
            if (isPushTimeOne || isPushTimeTwo || isPushTimeThree) {
                LogUtils.d("push le ");
//                BmobPushManager bmobPush1 = new BmobPushManager();
//                bmobPush1.pushMessageAll("Hello Bmob.");


                BmobPushManager bmobPush = new BmobPushManager();
                BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
                query.addWhereEqualTo("installId", installationId);
                bmobPush.setQuery(query);
                JSONObject data = new JSONObject();
                data.put("tag", "push");
                data.put("obj", "请及时提交论文状态");
                bmobPush.pushMessage(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断某一时间是否在一个区间内
     *
     * @param sourceTime 时间区间,半闭合,如[10:00-20:00)
     * @param curTime    需要判断的时间 如10:00
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isInTime(String sourceTime, String curTime) {
        if (sourceTime == null || !sourceTime.contains("-") || !sourceTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }
        if (curTime == null || !curTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + curTime);
        }
        String[] args = sourceTime.split("-");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            long now = sdf.parse(curTime).getTime();
            long start = sdf.parse(args[0]).getTime();
            long end = sdf.parse(args[1]).getTime();
            if (args[1].equals("00:00")) {
                args[1] = "24:00";
            }
            if (end < start) {
                if (now >= end && now < start) {
                    return false;
                } else {
                    return true;
                }
            } else {
                if (now >= start && now < end) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }

    }
}
