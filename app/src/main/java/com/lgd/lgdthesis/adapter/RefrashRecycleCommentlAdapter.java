package com.lgd.lgdthesis.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.bean.ArticleCommentBean;
import com.lgd.lgdthesis.view.CircleImageView;
import com.lgd.lgdthesis.view.TextViewPlus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蜗牛 on 2017-05-06.
 */

public class RefrashRecycleCommentlAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private LayoutInflater mInflater;
    private Context mContext;
    private List<ArticleCommentBean> mLists = new ArrayList<>();

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View mHeaderView;

    public RefrashRecycleCommentlAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }
    public void addItems(List<ArticleCommentBean> findCircleBeenList , boolean hasCleaner){
        if (hasCleaner){
            mLists.clear();
        }
        mLists.addAll(findCircleBeenList);
        notifyDataSetChanged();
    }
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }
    public View getHeaderView() {
        return mHeaderView;
    }
    private OnRecyclerViewMainListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewMainListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) return new NomalViewHolder(mHeaderView);
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_comment, parent, false);
        layout.setOnClickListener(this);
        return new NomalViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_HEADER) return;
        final int pos = getRealPosition(holder);
        holder.itemView.setTag(pos);
        ArticleCommentBean articleCommentBean = mLists.get(pos);
        if(holder instanceof NomalViewHolder) {
            ((NomalViewHolder) holder).tv_friend_name.setText(articleCommentBean.getFri_user_name());
            ((NomalViewHolder) holder).tv_comment_flour.setText(String.valueOf(pos+1)+"楼");
            ((NomalViewHolder) holder).tv_comment_time.setText(articleCommentBean.getTime());
            ((NomalViewHolder) holder).tv_comment_text.setText(articleCommentBean.getComment());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }
    @Override
    public void onClick(View v) {
        onRecyclerViewListener.onItemClick(v,(int)v.getTag());
    }
    public ArticleCommentBean getItem(int postion){
        return mLists.get(postion);
    }
    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mLists.size() : mLists.size() + 1;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class NomalViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView iv_friend_avator;
        public TextViewPlus tv_friend_name;
        public TextViewPlus tv_comment_flour;
        public TextViewPlus tv_comment_time;
        public TextViewPlus tv_comment_text;
        public NomalViewHolder(View view){
            super(view);
            iv_friend_avator = (CircleImageView) view.findViewById(R.id.iv_friend_article_anvator);
            tv_friend_name = (TextViewPlus) view.findViewById(R.id.tv_friend_user_name);
            tv_comment_flour = (TextViewPlus) view.findViewById(R.id.tv_item_article_comment_flour);
            tv_comment_time = (TextViewPlus) view.findViewById(R.id.tv_article_time);
            tv_comment_text = (TextViewPlus) view.findViewById(R.id.tv_item_article_comment_text);

        }
    }

}
