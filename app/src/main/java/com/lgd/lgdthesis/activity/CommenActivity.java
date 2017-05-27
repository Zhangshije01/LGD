package com.lgd.lgdthesis.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.util.TypedValue;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.adapter.RefrashRecycleCommentlAdapter;
import com.lgd.lgdthesis.base.BasesActivity;
import com.lgd.lgdthesis.bean.ArticleCommentBean;
import com.lgd.lgdthesis.databinding.ActivityCommenBinding;
import com.lgd.lgdthesis.utils.ToastUtils;
import com.lgd.lgdthesis.view.MyDecoration;
import com.lgd.lgdthesis.view.SwipeBackLayout;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class CommenActivity extends BasesActivity {

    ActivityCommenBinding mBinding;
    private SwipeBackLayout mSwipeBackLayout;
    private TextView tv;
    private RelativeLayout rl;
    private LinearLayoutManager linearLayoutManager;
    private RefrashRecycleCommentlAdapter adapter;
    private String article_name;
    public static void start(Context context, String article_name){
        Intent intent = new Intent(context,CommenActivity.class);
        intent.putExtra("article_name",article_name);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_commen);
        article_name = getIntent().getStringExtra("article_name");
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
        mBinding.swipeRefreshComment.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mBinding.swipeRefreshComment.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mBinding.swipeRefreshComment.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mBinding.recylerViewComment.setLayoutManager(linearLayoutManager);
//        mBinding.recylerViewMain.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL));
//        mBinding.recylerViewMain.addItemDecoration(new RecycleViewDivider(
//                mContext, LinearLayoutManager.VERTICAL, 10, getResources().getColor(R.color.color_view_red)));

//        //添加分割线
//        mBinding.recylerViewComment.addItemDecoration(new DividerItemDecoration(
//                this, DividerItemDecoration.HORIZONTAL));
        mBinding.recylerViewComment.setAdapter(adapter = new RefrashRecycleCommentlAdapter(this));
        mBinding.sb.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mBinding.sb.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mBinding.swipeRefreshComment.setRefreshing(true);
                //自动刷新
                queryArticle();
            }
        });
        mBinding.recylerViewComment.addItemDecoration(new MyDecoration(mContext, MyDecoration.VERTICAL_LIST));

        //下拉加载
        mBinding.swipeRefreshComment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                queryArticle();
            }
        });
    }

    public void queryArticle() {
        BmobQuery<ArticleCommentBean> query = new BmobQuery<>();
        query.addWhereEqualTo("article_name",article_name).order("createdAt")
                .findObjects(new FindListener<ArticleCommentBean>() {
                    @Override
                    public void done(List<ArticleCommentBean> list, BmobException e) {
                        if(e == null){
                            adapter.addItems(list, false);
                            mBinding.swipeRefreshComment.setRefreshing(false);
                        }else{
                            ToastUtils.show(e.getMessage());
                            return;
                        }
                    }
                });
    }

}
