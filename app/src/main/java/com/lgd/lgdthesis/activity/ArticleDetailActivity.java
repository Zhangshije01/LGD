package com.lgd.lgdthesis.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.base.BasesActivity;
import com.lgd.lgdthesis.bean.CommentBean;
import com.lgd.lgdthesis.bean.FindCircleBean;
import com.lgd.lgdthesis.cache.LGDSharedprefrence;
import com.lgd.lgdthesis.databinding.ActivityArticleDetailBinding;
import com.lgd.lgdthesis.utils.LogUtils;
import com.lgd.lgdthesis.utils.ToastUtils;
import com.lgd.lgdthesis.view.PingLunDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class ArticleDetailActivity extends BasesActivity {

    ActivityArticleDetailBinding mBinding;
    private FindCircleBean findCircleBean;
    private String curr_user_name;
    private boolean isCollect;
    private String objectId;
    public static void start(Context context, FindCircleBean findCircleBean){
        Intent intent = new Intent(context,ArticleDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("findCircleBean", findCircleBean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_article_detail);
        findCircleBean = (FindCircleBean) getIntent().getSerializableExtra("findCircleBean");
        curr_user_name = LGDSharedprefrence.getUserName();
        LogUtils.d("articledetailActivity"+findCircleBean.toString());


        mBinding.friendName.setText(findCircleBean.getUser_name());
        mBinding.tvArticleName.setText(findCircleBean.getArticle_name());
        mBinding.tvArticleTime.setText(findCircleBean.getTime());
        mBinding.tvArticleDetail.setText(findCircleBean.getDetails());

        mBinding.writeComment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                    PingLunDialog dialog = new PingLunDialog(ArticleDetailActivity.this, curr_user_name, new PingLunDialog.PriorityListener() {
                        @Override
                        public void refreshPriority() {
                            LogUtils.d("serpinglun");
                        }
                    },  findCircleBean.getUser_name(),findCircleBean.getArticle_name());
                    dialog.showChangeDialog();
                    dialog.popupInputMethodWindow();
                }
        });
        mBinding.ivArticleComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommenActivity.start(mContext,findCircleBean.getArticle_name());
            }
        });

        final UMImage thumb =  new UMImage(mContext, R.drawable.umeng_socialize_back_icon);
        mBinding.ivDeatilShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(ArticleDetailActivity.this
                ).withMedia(thumb)
                        .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN)
                        .setCallback(umShareListener).open();
            }
        });


        mBinding.ivArticleCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isCollect){
                    mBinding.ivArticleCollect.setImageResource(R.mipmap.img_viewpager_collection_yes);
                    final CommentBean commentBean = new CommentBean();
                    commentBean.setArticle_name(findCircleBean.getArticle_name());
                    commentBean.setCollect(true);
                    commentBean.setCru_userName(curr_user_name);
                    commentBean.setFriend_name(findCircleBean.getUser_name());
                    commentBean.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e == null){
                                ToastUtils.show("收藏成功");
                                objectId = commentBean.getObjectId();
                            }else{
                                ToastUtils.show("收藏失败"+e.getMessage());
                            }
                        }

                    });
                    isCollect = true;
                }else{
                    mBinding.ivArticleCollect.setImageResource(R.mipmap.img_viewpager_collection_no_white);
                    if(objectId != null){
                        final CommentBean commentBean = new CommentBean();
                        commentBean.setObjectId(objectId);
                        commentBean.delete(new UpdateListener() {

                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    ToastUtils.show("取消收藏成功");
                                    isCollect = false;
                                }else{
                                    ToastUtils.show("取消失败" + e.getMessage());
                                }
                            }

                        });
                    }

                }
            }
        });
    }


}
