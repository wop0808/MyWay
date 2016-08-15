package com.hkc.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiResult;
import com.hkc.fragment.Fragment_popup1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/10.
 */
public class Vp_AddressInfo_Adapter extends FragmentPagerAdapter {
    public List<Fragment> fragmentList = new ArrayList<>();

    public Vp_AddressInfo_Adapter(FragmentManager fm) {
        super(fm);
        for (int i = 0; i < 10; i++) {
            Fragment_popup1 fragment_popup1 = new Fragment_popup1();
            fragmentList.add(fragment_popup1);
        }
    }


    public void setPoiResult(PoiResult poiResult) {
        List<PoiInfo> allPoi = poiResult.getAllPoi();
        for (int i = 0; i < 10; i++) {
            Fragment f = fragmentList.get(i);
            if (f instanceof Fragment_popup1) {
                Fragment_popup1 fp1 = (Fragment_popup1) f;
                fp1.setPoiInfo(allPoi.get(i));
            }
        }
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
