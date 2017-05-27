package com.lgd.lgdthesis.fragment;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.activity.ChatActivity;
import com.lgd.lgdthesis.activity.SeakActivity;
import com.lgd.lgdthesis.activity.SearchActivity;
import com.lgd.lgdthesis.activity.SendArticleActivity;
import com.lgd.lgdthesis.activity.StatiticActivity;
import com.lgd.lgdthesis.base.BasesFragment;
import com.lgd.lgdthesis.cache.LGDSharedprefrence;
import com.lgd.lgdthesis.databinding.FragmentMessageBinding;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends BasesFragment {


    FragmentMessageBinding mBinding;
    private String userObjectId;
    private String username;
    private String useravator;
    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_message,container,false);
        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userObjectId = LGDSharedprefrence.getUserObjectId();
        username = LGDSharedprefrence.getUserName();
        useravator = LGDSharedprefrence.getUserAvator();

        mBinding.llMessageSendArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SendArticleActivity.class);
                mContext.startActivity(intent);
            }
        });
        mBinding.llMessageSeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SeakActivity.class);
                mContext.startActivity(intent);
            }
        });
        mBinding.llMessageJianxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BmobIMUserInfo info = new BmobIMUserInfo(userObjectId, username, useravator);
                //启动一个会话，实际上就是在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
                BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, null);
                Bundle bundle = new Bundle();
                bundle.putSerializable("c", c);
                startActivity(ChatActivity.class,bundle);
            }
        });
        mBinding.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.start(mContext);
            }
        });
        mBinding.tvMessageStatitic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatiticActivity.start(mContext);
            }
        });
    }

}
