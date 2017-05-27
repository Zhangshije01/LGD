package com.lgd.lgdthesis.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.bean.FindCircleBean;
import com.lgd.lgdthesis.bean.MainFindBean;
import com.lgd.lgdthesis.databinding.ActivityStatiticBinding;
import com.lgd.lgdthesis.utils.LogUtils;
import com.lgd.lgdthesis.utils.ToastUtils;
import com.lgd.lgdthesis.view.PieChartView;
import com.lgd.lgdthesis.view.SwipeBackLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class StatiticActivity extends AppCompatActivity {

    ActivityStatiticBinding mBinding;

    private int num;
    List<PieChartView.PieceDataHolder> pieceDataHolders = new ArrayList<>();

    private int[] text_color = {
            Color.RED, Color.BLUE, Color.GRAY, Color.GREEN, 0xFF77CCAA, 0xFF55dd11
    };

    public static void start(Context context) {
        Intent intent = new Intent(context, StatiticActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_statitic);
        mBinding.swipeBack.setOnSwipeBackListener(new SwipeBackLayout.SwipeBackListener() {
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
                mBinding.rl.setTranslationX(percent * 30 - 30);
                mBinding.tv.setAlpha(percent < 0.5 ? 0.1f : percent);
            }
        });

        queryNum();


    }

    public void queryNum() {
        BmobQuery<MainFindBean> query = new BmobQuery<>();
        query.groupby(new String[]{"user_name"}).findObjects(new FindListener<MainFindBean>() {
            @Override
            public void done(List<MainFindBean> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        LogUtils.d("select username" + list.size());
                        initData(list);
                    } else {
                        LogUtils.d("smile" + "查询成功，无数据返回");
                    }
                } else {
                    LogUtils.d("smile" + "错误码：" + e.getErrorCode() + "，错误描述：" + e.getMessage());
                }
            }
        });
    }

    public void querySize(final int i, List<MainFindBean> list) {
        BmobQuery<FindCircleBean> query = new BmobQuery<>();
        query.addWhereEqualTo("user_name", list.get(i).getUser_name())
                .findObjects(new FindListener<FindCircleBean>() {
                    @Override
                    public void done(List<FindCircleBean> list, BmobException e) {
                        if (e == null) {
                            LogUtils.d("detail activity" + list.size());
                            num = list.size();
                            LogUtils.d("num zjs~~" + num);

                            pieceDataHolders.add(new PieChartView.PieceDataHolder(num, text_color[i], list.get(i).getUser_name()+"提交论文数 "+num +"篇"));

                            mBinding.piechartView.setData(pieceDataHolders);

                        } else {
                            ToastUtils.show(e.getMessage());
                            return;
                        }
                    }
                });
    }

    public void initData(List<MainFindBean> user_name_list) {

        LogUtils.d("userName_ssize" + user_name_list.size());
        for (int i = 0; i < user_name_list.size(); i++) {
            querySize(i, user_name_list);
        }
    }


}
