package com.hkc.adapter;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiResult;
import com.hkc.fragment.Fragment_popup1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/10.
 */
public class Vp_AddressInfo_Adapter extends FragmentPagerAdapter {
    private String TAG = "crazyK";
    public List<Fragment> fragmentList ;
    private FragmentManager fm;
    private ProgressDialog progressDialog;

    public Vp_AddressInfo_Adapter(FragmentManager fm,ProgressDialog progressDialog) {
        super(fm);
        this.fm = fm;
        this.progressDialog = progressDialog;
        fragmentList = new ArrayList<>();
    }


    public void setPoiResult(PoiResult poiResult) {
//        Log.i(TAG, "size: "+poiResult.getAllPoi().size());
        List<PoiInfo> allPoi = poiResult.getAllPoi();
        fragmentList.clear();

        for (int i = 0; i < poiResult.getAllPoi().size(); i++) {
//            Log.e(TAG, len + "::::::::::::::::::");
            Fragment_popup1 fragment_popup1 = new Fragment_popup1();
            fragment_popup1.setPoiInfo(allPoi.get(i));
            fragmentList.add(fragment_popup1);
        }

        notifyDataSetChanged();

        progressDialog.dismiss();

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
