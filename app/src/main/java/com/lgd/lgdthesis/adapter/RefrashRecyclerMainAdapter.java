package com.lgd.lgdthesis.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.bean.MainFindBean;
import com.lgd.lgdthesis.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蜗牛 on 2017-05-24.
 */

public class RefrashRecyclerMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private LayoutInflater mInflater;
    private Context mContext;
    private List<MainFindBean> mLists = new ArrayList<>();

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    private View mHeaderView;

    public RefrashRecyclerMainAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }
    public void addItems(List<MainFindBean> findCircleBeenList , boolean hasCleaner){
        if (hasCleaner){
            mLists.clear();
        }
        mLists.addAll(findCircleBeenList);
        notifyDataSetChanged();
    }

    public MainFindBean getItem(int postion){
        return mLists.get(postion);
    }
    private OnRecyclerViewMainListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewMainListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }
    public View getHeaderView() {
        return mHeaderView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) return new RefrashRecyclerMainAdapter.NomalViewHolder(mHeaderView);
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_circle_layout, parent, false);
        layout.setOnClickListener(this);
        return new RefrashRecyclerMainAdapter.NomalViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_HEADER) return;
        final int pos = getRealPosition(holder);
        holder.itemView.setTag(pos);

        MainFindBean mainFindBean = mLists.get(pos);
        if(holder instanceof RefrashRecyclerMainAdapter.NomalViewHolder) {
            if(mainFindBean.getUser_name()!=null){
                if(TextUtils.isEmpty(mainFindBean.getUser_name())){
                    ((NomalViewHolder) holder).tv_main_name.setText("匿名");
                }else{
                    ((NomalViewHolder) holder).tv_main_name.setText(mainFindBean.getUser_name());
                }
            }


            if(TextUtils.isEmpty(mainFindBean.getUser_avator())){
                ((NomalViewHolder) holder).iv_main_anvator.setImageResource(R.mipmap.icon_user_advator);
            }else{
                // 利用ImageLoader将用户选取的图片放到ivAvatar中显示
                ImageLoader.getInstance().displayImage(mainFindBean.getUser_avator(), ((NomalViewHolder) holder).iv_main_anvator);
            }

            if(TextUtils.isEmpty(mainFindBean.getArticle_name())){
                ((NomalViewHolder) holder).tv_main_title.setText("OKHttp网络访问框架");
            }else{
                ((NomalViewHolder) holder).tv_main_title.setText(mainFindBean.getArticle_name());
            }


        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mLists.size() : mLists.size() + 1;
    }

    @Override
    public void onClick(View v) {
        onRecyclerViewListener.onItemClick(v,(int)v.getTag());
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class NomalViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView iv_main_anvator;
        public TextView tv_main_name;
        public TextView tv_main_title;

        public NomalViewHolder(View view){
            super(view);
            iv_main_anvator = (CircleImageView) view.findViewById(R.id.iv_item_main_article_anvator);
            tv_main_name = (TextView) view.findViewById(R.id.tv_item_main_article_name);
            tv_main_title = (TextView) view.findViewById(R.id.tv_item_main_article_title);

        }
    }

}
