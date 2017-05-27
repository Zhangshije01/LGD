package com.lgd.lgdthesis.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.bean.FindCircleBean;
import com.lgd.lgdthesis.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蜗牛 on 2017-05-06.
 */

public class RefrashRecyclerFindAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private LayoutInflater mInflater;
    private Context mContext;
    private List<FindCircleBean> mLists = new ArrayList<>();

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View mHeaderView;

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
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_circle_layout, parent, false);
        layout.setOnClickListener(this);
        return new NomalViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_HEADER) return;
        final int pos = getRealPosition(holder);
        holder.itemView.setTag(pos);
        FindCircleBean findCircleBean = mLists.get(pos);
        if(holder instanceof NomalViewHolder) {
            ((NomalViewHolder) holder).tv_find_name.setText(findCircleBean.getUser_name());
            ((NomalViewHolder) holder).tv_find_time.setText(findCircleBean.getTime());
            ((NomalViewHolder) holder).tv_find_type.setText(findCircleBean.getArticle_type());
            ((NomalViewHolder) holder).tv_find_title.setText(findCircleBean.getDetails());
            ((NomalViewHolder) holder).tv_find_hasRead.setText("阅读 "+findCircleBean.getHasReaded()+"*");
            ((NomalViewHolder) holder).tv_find_hasComment.setText("评论 "+findCircleBean.getHasComment()+"*");
            ((NomalViewHolder) holder).tv_find_hasFlad.setText("喜欢 "+findCircleBean.getHasFlad()+"*");
            ((NomalViewHolder) holder).tv_find_hasZan.setText("赞 "+findCircleBean.getHasZan());
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
    public FindCircleBean getItem(int postion){
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

}
