package com.lgd.lgdthesis.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.adapter.RefrashRecyclerFindAdapter;
import com.lgd.lgdthesis.base.BasesActivity;
import com.lgd.lgdthesis.bean.FindCircleBean;
import com.lgd.lgdthesis.databinding.ActivityFindFriendBinding;
import com.lgd.lgdthesis.utils.ToastUtils;
import com.lgd.lgdthesis.view.SwipeBackLayout;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FindFriendActivity extends BasesActivity {
    ActivityFindFriendBinding mBinding;
    private SwipeBackLayout mSwipeBackLayout;
    private TextView tv;
    private RelativeLayout rl;
    private LinearLayoutManager linearLayoutManager;
    private RefrashRecyclerFindAdapter adapter;
    private int i =0;
    public static void start(Context context){
        Intent intent = new Intent(context,FindFriendActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_find_friend);

        rl = (RelativeLayout) findViewById(R.id.rl);
        tv = (TextView) findViewById(R.id.tv);
        tv.animate().rotation(-90);
        mSwipeBackLayout = (SwipeBackLayout) findViewById(R.id.sb);
        mSwipeBackLayout.setOnSwipeBackListener(new SwipeBackLayout.SwipeBackListener() {
            @Override
            public void onOpen() {
                finish();
                overridePendingTransition(0, R.anim.right_anim);
            }

            @Override
            public void onClose() {

            }

            @Override
            public void onSwipe(float percent) {
                rl.setTranslationX(percent * 30 - 30);
                tv.setAlpha(percent<0.5?0.1f:percent);
            }
        });
        setSwipLayout();
    }
    public void setSwipLayout(){
        mBinding.swipeRefreshAddFriend.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mBinding.swipeRefreshAddFriend.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mBinding.swipeRefreshAddFriend.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mBinding.recylerViewAddFriend.setLayoutManager(linearLayoutManager);
//        mBinding.recylerViewMain.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL));
//        mBinding.recylerViewMain.addItemDecoration(new RecycleViewDivider(
//                mContext, LinearLayoutManager.VERTICAL, 10, getResources().getColor(R.color.color_view_red)));

        //添加分割线
        mBinding.recylerViewAddFriend.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.HORIZONTAL));
        mBinding.recylerViewAddFriend.setAdapter(adapter = new RefrashRecyclerFindAdapter(this));
        mBinding.sb.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mBinding.sb.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mBinding.swipeRefreshAddFriend.setRefreshing(true);
                //自动刷新
                queryArticle(1);
            }
        });
        //下拉加载
        mBinding.swipeRefreshAddFriend.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                i++;
                queryArticle(i);
            }
        });
        mBinding.editSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.start(mContext);
            }
        });
    }

    public void queryArticle(int i) {
        BmobQuery<FindCircleBean> query = new BmobQuery<>();
        query.setLimit(10).setSkip(10*(i-1)).order("-createdAt")
                .findObjects(new FindListener<FindCircleBean>() {
                    @Override
                    public void done(List<FindCircleBean> list, BmobException e) {
                        if(e == null){
                            adapter.addItems(list, false);
                            mBinding.swipeRefreshAddFriend.setRefreshing(false);
                        }else{
                            ToastUtils.show(e.getMessage());
                            return;
                        }
                    }
                });
    }
}
