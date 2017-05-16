package com.lgd.lgdthesis.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蜗牛 on 2017-05-13.
 */

public class UserDetailViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mfragment_List = new ArrayList<>();

    public UserDetailViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment) {
        if (fragment != null) {
            this.mfragment_List.add(fragment);
        }
    }


    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return mfragment_List.get(position);
    }

    @Override
    public int getCount() {
        return mfragment_List.size();
    }
}
