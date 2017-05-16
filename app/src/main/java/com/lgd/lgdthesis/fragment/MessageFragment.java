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
import com.lgd.lgdthesis.activity.SeakActivity;
import com.lgd.lgdthesis.activity.SendArticleActivity;
import com.lgd.lgdthesis.base.BasesFragment;
import com.lgd.lgdthesis.databinding.FragmentMessageBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends BasesFragment {


    FragmentMessageBinding mBinding;
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
    }
}
