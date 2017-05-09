package com.lgd.lgdthesis.fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lgd.lgdthesis.R;
import com.lgd.lgdthesis.activity.UserDetailsActivity;
import com.lgd.lgdthesis.databinding.FragmentUserBinding;

public class UserFragment extends Fragment {
    FragmentUserBinding mBinding;
    public UserFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_user,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.llUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDetailsActivity.start(getContext());
            }
        });
    }
}
