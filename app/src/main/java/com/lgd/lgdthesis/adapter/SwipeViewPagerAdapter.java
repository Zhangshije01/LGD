package com.lgd.lgdthesis.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lgd.lgdthesis.R;

import java.util.List;

/**
 * Created by 蜗牛 on 2017-05-17.
 */

public class SwipeViewPagerAdapter extends PagerAdapter{
    public List<Integer> comm_data_ls;
    private Context context;
    private View itemView;

    public SwipeViewPagerAdapter(Context context, List<Integer> comm_data_ls) {
        this.context = context;
        this.comm_data_ls = comm_data_ls;
    }

    @Override
    public int getCount() {
        return comm_data_ls.size() > 0 ? comm_data_ls.size() == 1 ? 1 : Integer.MAX_VALUE : 0;
    }

    public Object instantiateItem(ViewGroup container, final int position) {
        itemView = View.inflate(context, R.layout.item_viewpage_content, null);
        ImageView imageView = ((ImageView) itemView.findViewById(R.id.iv_viewpager_image));
        imageView.setBackgroundResource(comm_data_ls.get(position % comm_data_ls.size()));
        container.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object obj) {
//        container.removeView(itemView);
    }

    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == (arg1);
    }
}
