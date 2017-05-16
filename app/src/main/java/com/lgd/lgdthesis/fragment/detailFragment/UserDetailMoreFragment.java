package com.lgd.lgdthesis.fragment.detailFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lgd.lgdthesis.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserDetailMoreFragment extends Fragment {


    public UserDetailMoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_detail_more, container, false);
    }

}
