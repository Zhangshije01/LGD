package com.lgd.lgdthesis.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lgd.lgdthesis.bean.FindCircleBean;
import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蜗牛 on 2017-05-06.
 */

public class RefrashRecyclerFindAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private LayoutInflater mInflater;
    private Context mContext;
    private List<FindCircleBean> mLists = new ArrayList<>();
    private static final int HEADER_ITEM = 0;//表示有头布局
    private static final int FOOTER_ITEM = 0;//表示有底布局
    private static final int NOMAL_ITEM = 1;//表示没有头和底布局
    private View view_header;//头布局
    private View view_footer;//底布局
    public RefrashRecyclerFindAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }
    public void addItems(List<FindCircleBean> findCircleBeenList , boolean hasCleaner){
        if (hasCleaner){
            mLists.clear();
        }
        mLists.addAll(findCircleBeenList);
        notifyDataSetChanged();
    }

    public View getView_header() {
        return view_header;
    }

    public void setView_header(View view_header) {
        this.view_header = view_header;
        notifyItemInserted(0);
    }

    public View getView_footer() {
        return view_footer;
    }

    public void setView_footer(View view_footer) {
        this.view_footer = view_footer;
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(view_header != null && viewType == HEADER_ITEM) {
            return new NomalViewHolder(view_header);
        }
        if(view_footer != null && viewType == FOOTER_ITEM){
            return new NomalViewHolder(view_footer);
        }
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_circle_layout, parent, false);
        return new NomalViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == NOMAL_ITEM){
            if(holder instanceof NomalViewHolder) {
                //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
//                ((ListHolder) holder).tv.setText(mDatas.get(position-1));
                FindCircleBean findCirclebean = mLists.get(position - 1);
                return;
            }
            return;
        }else if(getItemViewType(position) == HEADER_ITEM){
            return;
        }else{
            return;
        }
    }

    /** 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view    * */
    @Override
    public int getItemViewType(int position) {
        if (view_header == null && view_footer == null){
            return NOMAL_ITEM;
        }
        if (position == 0){
            //第一个item应该加载Header
            return HEADER_ITEM;
        }
        if (position == getItemCount()-1){
            //最后一个,应该加载Footer
            return FOOTER_ITEM;
        }
        return NOMAL_ITEM;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class NomalViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView iv_find_anvator;
        public TextView tv_find_name;
        public TextView tv_find_time;
        public TextView tv_find_title;
        public TextView tv_find_type;
        public TextView tv_find_hasRead;
        public TextView tv_find_hasComment;
        public TextView tv_find_hasFlad;
        public TextView tv_find_hasZan;
        public NomalViewHolder(View view){
            super(view);
            if(view == view_header){
                return;
            }
            if(view == view_footer){
                return;
            }
            iv_find_anvator = (CircleImageView) view.findViewById(R.id.iv_item_find_article_anvator);
            tv_find_name = (TextView) view.findViewById(R.id.tv_item_find_article_name);
            tv_find_time = (TextView) view.findViewById(R.id.tv_item_find_article_time);
            tv_find_title = (TextView) view.findViewById(R.id.tv_item_find_article_title);
            tv_find_type = (TextView) view.findViewById(R.id.tv_item_find_article_type);
            tv_find_hasRead = (TextView) view.findViewById(R.id.tv_item_find_article_hasRead);
            tv_find_hasComment = (TextView) view.findViewById(R.id.tv_item_find_article_hasComment);
            tv_find_hasFlad = (TextView) view.findViewById(R.id.tv_item_find_article_hasFlad);
            tv_find_hasZan = (TextView) view.findViewById(R.id.tv_item_find_article_hasZan);
        }
    }

    //返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
    @Override
    public int getItemCount() {
        if(view_header == null && view_footer == null){
            return mLists.size();
        }else if(view_header == null && view_footer != null){
            return mLists.size() + 1;
        }else if (view_header != null && view_footer == null){
            return mLists.size() + 1;
        }else {
            return mLists.size() + 2;
        }
    }

}
