package com.lgd.lgdthesis.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lgd.lgdthesis.bean.FindCircleBean;
import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.adapter.RefrashRecyclerFindAdapter;
import com.lgd.lgdthesis.databinding.FragmentFindBinding;
import com.lgd.lgdthesis.view.ColumnHorizontalScrollView;

import java.util.ArrayList;
import java.util.List;


public class FindFragment extends Fragment {
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

    public FindFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        initHeaderScrollView();
        mBinding.swipeRefreshFind.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.getRoot().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //TODO 添加数据
                        List<FindCircleBean> lists = new ArrayList<FindCircleBean>();
//                        adapter.addItems();
                        mBinding.swipeRefreshFind.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    private void setHeaderView(RecyclerView view) {
        item_find_header_tab = LayoutInflater.from(getContext()).inflate(R.layout.item_find_header_view_tab, view, false);
        adapter.setView_header(item_find_header_tab);
    }

    //TODO 添加底部布局
    private void setFooterView(RecyclerView view) {

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
        userChannelList.add("哈哈哈");
        userChannelList.add("啊啊啊");
        userChannelList.add("不不不");
        userChannelList.add("嗯嗯嗯");
        userChannelList.add("通天塔");
        userChannelList.add("羊羊羊");
        userChannelList.add("去去去");
        mRadioGroup_content.removeAllViews();
        int count = userChannelList.size();
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.rightMargin = 10;
            params.topMargin = 10;
            params.bottomMargin = 10;
            TextView columnTextView = new TextView(getActivity());
            columnTextView.setGravity(Gravity.CENTER_VERTICAL);
            columnTextView.setId(i);
            columnTextView.setTextSize(19);
            columnTextView.setBackgroundResource(R.drawable.shap_find_article_type);
            columnTextView.setPadding(5,5,5,5);
            columnTextView.setText(userChannelList.get(i));
            mRadioGroup_content.addView(columnTextView, i, params);
        }


    }
}
