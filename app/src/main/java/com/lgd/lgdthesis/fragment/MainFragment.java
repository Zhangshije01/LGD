package com.lgd.lgdthesis.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.activity.FindFriendActivity;
import com.lgd.lgdthesis.activity.FriendDetailActivity;
import com.lgd.lgdthesis.activity.SearchActivity;
import com.lgd.lgdthesis.adapter.OnRecyclerViewMainListener;
import com.lgd.lgdthesis.adapter.RefrashRecyclerMainAdapter;
import com.lgd.lgdthesis.base.BasesFragment;
import com.lgd.lgdthesis.bean.MainFindBean;
import com.lgd.lgdthesis.databinding.FragmentMainBinding;
import com.lgd.lgdthesis.utils.LogUtils;
import com.lgd.lgdthesis.view.MyDecoration;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BasesFragment {

    FragmentMainBinding mBinding;
    private LinearLayoutManager linearLayoutManager;
    private RefrashRecyclerMainAdapter adapter;
    private View item_find_header_tab;
    private int i = 1;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.ivMainFragmentAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindFriendActivity.start(getContext());
            }
        });
        mBinding.swipeRefreshMain.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mBinding.swipeRefreshMain.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mBinding.swipeRefreshMain.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mBinding.recylerViewMain.setLayoutManager(linearLayoutManager);
//        mBinding.recylerViewMain.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL));
//        mBinding.recylerViewMain.addItemDecoration(new RecycleViewDivider(
//                mContext, LinearLayoutManager.VERTICAL, 10, getResources().getColor(R.color.color_view_red)));

        //添加分割线
        mBinding.recylerViewMain.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.HORIZONTAL));
        mBinding.recylerViewMain.setAdapter(adapter = new RefrashRecyclerMainAdapter(getContext()));
        setHeaderView(mBinding.recylerViewMain);
        mBinding.recylerViewMain.addItemDecoration(new MyDecoration(mContext, MyDecoration.VERTICAL_LIST));


        //下拉加载
        mBinding.swipeRefreshMain.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                queryArticle();
                setlect();
            }
        });

        //设置RecyclerView的点击事件
        adapter.setOnRecyclerViewListener(new OnRecyclerViewMainListener() {
            @Override
            public void onItemClick(View view, int position) {
                MainFindBean mainFindBean = adapter.getItem(position);
                LogUtils.d("dianji" + position);
                LogUtils.d(mainFindBean.getUser_name());
                FriendDetailActivity.start(mContext, mainFindBean.getUser_name());

            }
        });
        mBinding.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.start(mContext);
            }
        });
        setlect();
    }

    public void setlect() {

//        String sql = "select * from MainFindBean group by user_name order by time desc";
//        new BmobQuery<MainFindBean>().doSQLQuery(sql, new SQLQueryListener<MainFindBean>() {
//            @Override
//            public void done(BmobQueryResult<MainFindBean> result, BmobException e) {
//                if (e == null) {
//                    List<MainFindBean> list = (List<MainFindBean>) result.getResults();
//                    if (list != null && list.size() > 0) {
//                        LogUtils.d("select" + list.size());
//                        LogUtils.d("select" + list.get(0).toString());
//                        adapter.addItems(list, true);
//                        mBinding.swipeRefreshMain.setRefreshing(false);
//                    } else {
//                        LogUtils.d("smile" + "查询成功，无数据返回");
//                    }
//                } else {
//                    LogUtils.d("smile" + "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
//                }
//            }
//        });
        BmobQuery<MainFindBean> query = new BmobQuery<>();
        query.groupby(new String[]{"user_name"}).findObjects(new FindListener<MainFindBean>() {
            @Override
            public void done(List<MainFindBean> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        LogUtils.d("select" + list.size());
                        LogUtils.d("select" + list.get(0).toString());
                        adapter.addItems(list, true);
                        mBinding.swipeRefreshMain.setRefreshing(false);
                    } else {
                        LogUtils.d("smile" + "查询成功，无数据返回");
                    }
                } else {
                    LogUtils.d("smile" + "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        });
    }

    private void setHeaderView(RecyclerView view) {
        item_find_header_tab = LayoutInflater.from(getContext()).inflate(R.layout.item_main_header_view, view, false);
        adapter.setHeaderView(item_find_header_tab);
    }


//    public void queryArticle() {
//        BmobQuery<MainFindBean> query = new BmobQuery<>();
//        query.findObjects(new FindListener<MainFindBean>() {
//            @Override
//            public void done(List<MainFindBean> list, BmobException e) {
//                if(e == null){
//                    adapter.addItems(list, true);
//                    mBinding.swipeRefreshMain.setRefreshing(false);
//                }else{
//                    ToastUtils.show(e.getMessage());
//                    return;
//                }
//            }
//        });
//    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.llMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mBinding.llMain.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mBinding.swipeRefreshMain.setRefreshing(true);
                //自动刷新
//                queryArticle();
                setlect();
            }
        });
    }
}
