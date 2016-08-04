package com.hkc.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/4.
 */
public class FragAdapter_Popup extends FragmentPagerAdapter {

    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

    public FragAdapter_Popup(FragmentManager fm) {
        super(fm);
        for (int i = 0;i < 10  ;i++ ){
            Fragment_popup1 fragment_popup1 = new Fragment_popup1();
            fragmentArrayList.add(fragment_popup1);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }
}
