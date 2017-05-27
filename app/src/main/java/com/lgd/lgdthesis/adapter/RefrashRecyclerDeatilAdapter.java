package com.lgd.lgdthesis.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.bean.FindCircleBean;
import com.lgd.lgdthesis.view.TextViewPlus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蜗牛 on 2017-05-06.
 */

public class RefrashRecyclerDeatilAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private LayoutInflater mInflater;
    private Context mContext;
    private List<FindCircleBean> mLists = new ArrayList<>();

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View mHeaderView;

    public RefrashRecyclerDeatilAdapter(Context context) {
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
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_detail, parent, false);
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
            ((RefrashRecyclerDeatilAdapter.NomalViewHolder) holder).tv_friend_time.setText(findCircleBean.getCreatedAt());
            ((RefrashRecyclerDeatilAdapter.NomalViewHolder) holder).tv_friend_name.setText(findCircleBean.getArticle_name());
            ((RefrashRecyclerDeatilAdapter.NomalViewHolder) holder).tv_friend_flover.setText("喜欢"+findCircleBean.getHasFlad());
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
        public TextViewPlus tv_friend_time;
        public TextViewPlus tv_friend_name;
        public TextViewPlus tv_friend_flover;
        public NomalViewHolder(View view){
            super(view);
            tv_friend_time = (TextViewPlus) view.findViewById(R.id.tv_friend_detail_time);
            tv_friend_name = (TextViewPlus) view.findViewById(R.id.tv_friend_detail_aritcle);
            tv_friend_flover = (TextViewPlus) view.findViewById(R.id.tv_friend_detail_flover);

        }
    }

}
