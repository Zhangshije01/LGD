package com.lgd.lgdthesis.adapter;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xyzlf.autoplay.viewpager.AutoPagerAdapter;

import java.util.List;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by 蜗牛 on 2017-05-17.
 */

public class AutoPlayPagerAdapter extends AutoPagerAdapter {
    private List<Integer> list;

    public AutoPlayPagerAdapter(List<Integer> list) {
        this.list = list;
    }

    @Override
    public View getView(LayoutInflater layoutInflater, int position) {
        ImageView imageView = new ImageView(getApplicationContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(params);
        int resId = list.get(getPositionForIndicator(position));
        imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), resId));
        return imageView;
    }

    @Override
    public int getDataCount() {
        return list == null ? 0 : list.size();
    }
}
