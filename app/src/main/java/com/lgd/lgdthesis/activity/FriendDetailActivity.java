package com.lgd.lgdthesis.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.adapter.OnRecyclerViewMainListener;
import com.lgd.lgdthesis.adapter.RefrashRecyclerDeatilAdapter;
import com.lgd.lgdthesis.base.BasesActivity;
import com.lgd.lgdthesis.bean.FindCircleBean;
import com.lgd.lgdthesis.databinding.ActivityFriendDetailBinding;
import com.lgd.lgdthesis.utils.LogUtils;
import com.lgd.lgdthesis.utils.ToastUtils;
import com.lgd.lgdthesis.view.MyDecoration;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FriendDetailActivity extends BasesActivity {

    ActivityFriendDetailBinding mBinding;
    private LinearLayoutManager linearLayoutManager;
    private RefrashRecyclerDeatilAdapter adapter;
    private String user_name;
    public static void start(Context context,String user_name){
        Intent intent = new Intent(context,FriendDetailActivity.class);
        intent.putExtra("user_name",user_name);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_friend_detail);
        setSwipLayout();
        user_name = getIntent().getStringExtra("user_name");

        LogUtils.d("friend"+user_name);
        mBinding.tvFriendDetailUserName.setText(user_name);

        mBinding.ivFriendDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void setSwipLayout(){
        mBinding.swipeRefreshFriendDetail.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mBinding.swipeRefreshFriendDetail.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mBinding.swipeRefreshFriendDetail.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mBinding.recylerViewFriendDetail.setLayoutManager(linearLayoutManager);
//        mBinding.recylerViewMain.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL));
//        mBinding.recylerViewMain.addItemDecoration(new RecycleViewDivider(
//                mContext, LinearLayoutManager.VERTICAL, 10, getResources().getColor(R.color.color_view_red)));

        //添加分割线
        mBinding.recylerViewFriendDetail.addItemDecoration(new MyDecoration(mContext, MyDecoration.VERTICAL_LIST));

        mBinding.recylerViewFriendDetail.setAdapter(adapter = new RefrashRecyclerDeatilAdapter(this));
        mBinding.llFriendDetail.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mBinding.llFriendDetail.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mBinding.swipeRefreshFriendDetail.setRefreshing(true);
                //自动刷新
                queryArticle();
            }
        });

        //下拉加载
        mBinding.swipeRefreshFriendDetail.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryArticle();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewMainListener() {
            @Override
            public void onItemClick(View view, int position) {
                FindCircleBean findCircleBean = adapter.getItem(position);
                ArticleDetailActivity.start(mContext,findCircleBean);
            }
        });

    }
    public void queryArticle() {
        BmobQuery<FindCircleBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user_name", user_name).order("-createdAt")
                .findObjects(new FindListener<FindCircleBean>() {
                    @Override
                    public void done(List<FindCircleBean> list, BmobException e) {
                        if(e == null){
                            LogUtils.d("detail activity"+list.size());
                            adapter.addItems(list, true);
                            mBinding.swipeRefreshFriendDetail.setRefreshing(false);
                        }else{
                            ToastUtils.show(e.getMessage());
                            return;
                        }
                    }
                });
    }
}
