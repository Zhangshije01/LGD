package com.lgd.lgdthesis.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.activity.ArticleDetailActivity;
import com.lgd.lgdthesis.adapter.AutoPlayPagerAdapter;
import com.lgd.lgdthesis.adapter.OnRecyclerViewMainListener;
import com.lgd.lgdthesis.adapter.RefrashRecyclerFindAdapter;
import com.lgd.lgdthesis.base.BasesFragment;
import com.lgd.lgdthesis.bean.FindCircleBean;
import com.lgd.lgdthesis.broadcast.LGDPushReceiver;
import com.lgd.lgdthesis.databinding.FragmentFindBinding;
import com.lgd.lgdthesis.utils.LogUtils;
import com.lgd.lgdthesis.utils.ToastUtils;
import com.lgd.lgdthesis.view.ColumnHorizontalScrollView;
import com.lgd.lgdthesis.view.MyDecoration;
import com.xyzlf.autoplay.viewpager.CustomerBanner;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class FindFragment extends BasesFragment implements LGDPushReceiver.EventListener{
    FragmentFindBinding mBinding;

    private LinearLayoutManager linearLayoutManager;
    private RefrashRecyclerFindAdapter adapter;
    private View item_find_header_tab;

    private ImageView shade_left;
    private ImageView shade_right;
    private int mScreenWidth = 0;
    private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    private LinearLayout mRadioGroup_content;
    private RelativeLayout mrl_collum;
    private List<FindCircleBean> findCircleBeen;
    private int i=1;

    public FindFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_find, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.swipeRefreshFind.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mBinding.swipeRefreshFind.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mBinding.swipeRefreshFind.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);


        mBinding.recylerViewFind.setLayoutManager(linearLayoutManager);
        mBinding.recylerViewFind.setAdapter(adapter = new RefrashRecyclerFindAdapter(getContext()));
        setHeaderView(mBinding.recylerViewFind);
        setFooterView(mBinding.recylerViewFind);
        mBinding.recylerViewFind.addItemDecoration(new MyDecoration(mContext, MyDecoration.VERTICAL_LIST));
        initHeaderScrollView();
        initHeaderView();


        //下拉加载
        mBinding.swipeRefreshFind.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                i++;
                queryArticle();
            }
        });

        //设置RecyclerView的点击事件
        adapter.setOnRecyclerViewListener(new OnRecyclerViewMainListener() {
            @Override
            public void onItemClick(View view, int position) {
                FindCircleBean findCircleBean = adapter.getItem(position);
                LogUtils.d("dianji"+position);
                ArticleDetailActivity.start(mContext,findCircleBean);

            }
        });

    }

    private void setHeaderView(RecyclerView view) {
        item_find_header_tab = LayoutInflater.from(getContext()).inflate(R.layout.item_find_header_view_tab, view, false);
        adapter.setHeaderView(item_find_header_tab);
    }


    //TODO 添加底部布局
    private void setFooterView(RecyclerView view) {

    }

    public void initHeaderView() {
        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.timg_one);
        list.add(R.mipmap.timg_three);
        list.add(R.mipmap.timg_seven);

        CustomerBanner banner = (CustomerBanner) item_find_header_tab.findViewById(R.id.banner_header_view);
        banner.setAdapter(new AutoPlayPagerAdapter(list));
    }

    private void initHeaderScrollView() {
        mColumnHorizontalScrollView = (ColumnHorizontalScrollView) item_find_header_tab.findViewById(R.id.mColumnHorizontalScrollView);
        mRadioGroup_content = (LinearLayout) item_find_header_tab.findViewById(R.id.mRadioGroup_content);
        mrl_collum = (RelativeLayout) item_find_header_tab.findViewById(R.id.rl_column);
        mColumnHorizontalScrollView.setParam(getActivity(), mScreenWidth,
                mRadioGroup_content,
                mrl_collum);

        //TODO list
        List<String> userChannelList = new ArrayList<>();
        userChannelList.add("运动&健身");
        userChannelList.add("旅行*在路上");
        userChannelList.add("万物生灵");
        userChannelList.add("@产品");
        userChannelList.add("美人说");
        userChannelList.add("人物");
        userChannelList.add("@IT互联网");
        mRadioGroup_content.removeAllViews();
        int count = userChannelList.size();
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 30;
            params.rightMargin = 30;
            params.topMargin = 10;
            params.bottomMargin = 10;
            TextView columnTextView = new TextView(getActivity());
            columnTextView.setGravity(Gravity.CENTER_VERTICAL);
            columnTextView.setId(i);
            columnTextView.setTextSize(18);
            columnTextView.setBackgroundResource(R.drawable.shap_find_article_type);
            columnTextView.setPadding(20, 10, 20, 10);
            columnTextView.setTextColor(getResources().getColor(R.color.color_view_red));
            columnTextView.setText(userChannelList.get(i));
            mRadioGroup_content.addView(columnTextView, i, params);
        }


    }
    public void queryArticle() {
        BmobQuery<FindCircleBean> query = new BmobQuery<>();
        query.order("-createdAt")
                .findObjects(new FindListener<FindCircleBean>() {
                    @Override
                    public void done(List<FindCircleBean> list, BmobException e) {
                        if(e == null){
                            adapter.addItems(list, true);
                            mBinding.swipeRefreshFind.setRefreshing(false);
                        }else{
                            ToastUtils.show(e.getMessage());
                            return;
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.llFind.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mBinding.llFind.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mBinding.swipeRefreshFind.setRefreshing(true);
                //自动刷新
                queryArticle();

            }
        });
        LGDPushReceiver.regist(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LGDPushReceiver.unRegist(this);
    }

    @Override
    public void onNewPost() {
        LogUtils.d("什么调集吧玩意");

    }

    @Override
    public void onAtOne() {
        LogUtils.d("什么调集吧玩意");
    }
}
