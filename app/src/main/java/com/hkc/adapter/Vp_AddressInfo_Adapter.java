package com.hkc.adapter;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

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
//    public Fragment[] fragmentList;
    private FragmentManager fm;
//    private ProgressDialog progressDialog;

    private PoiResult poiResult;

    public Vp_AddressInfo_Adapter(FragmentManager fm/*,ProgressDialog rogressDialog*/) {
        super(fm);
        this.fm = fm;
//        this.progressDialog = progressDialog;
//        fragmentList = new ArrayList<>();
    }


//    public void temp(){
//        for (int i = 0; i < poiResult.getAllPoi().size(); i++) {
////            Log.e(TAG, len + "::::::::::::::::::");
//            Fragment_popup1 fragment_popup1 = new Fragment_popup1();
//            fragment_popup1.setPoiInfo(allPoi.get(i));
//            fragmentList.add(fragment_popup1);
//        }
//    }

//    public void clearFragemnts() {
////        fragmentList = null;
//        notifyDataSetChanged();
//    }

    public void setPoiResult(PoiResult poiResult) {
//        Log.i("MainActivity", "位子大小2：" + poiResult.getAllPoi().get(0).name);
        this.poiResult = poiResult;
        notifyDataSetChanged();
//        Log.i("MainActivity", "位子大小3：" + this.poiResult.getAllPoi().get(0).name);
//        fragmentList = new Fragment[this.poiResult.getAllPoi().size()];
//        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }


    /**蛟神黑科技*/
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);

        String fragmentTag = fragment.getTag();

        FragmentTransaction ft =fm.beginTransaction();

        //移除旧的fragment

        ft.remove(fragment);

        Fragment_popup1 fp1 = new Fragment_popup1();
//        Log.i("MainActivity", "位子大小4：" + this.poiResult.getAllPoi().get(position).name);
        fp1.setPoiInfo(poiResult.getAllPoi().get(position));

        fragment = fp1;


        ft.add(container.getId(),fragment,fragmentTag);
        ft.attach(fragment);
        ft.commitAllowingStateLoss();



        return fragment;
    }



    @Override
    public Fragment getItem(int position) {
//        if (null == fragmentList[position]) {
            Fragment_popup1 fp1 = new Fragment_popup1();
//            Log.i("MainActivity", "位子大小4：" + this.poiResult.getAllPoi().get(position).name);
            fp1.setPoiInfo(poiResult.getAllPoi().get(position));
//            fragmentList[position] = fp1;
//        }
        return fp1;
    }

    @Override
    public int getCount() {
        return poiResult == null ? 0 :(this.poiResult.getAllPoi().size());
    }
}
