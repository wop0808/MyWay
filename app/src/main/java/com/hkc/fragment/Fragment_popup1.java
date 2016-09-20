package com.hkc.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiResult;
import com.hkc.mymapy.MainActivity;
import com.hkc.mymapy.R;

/**
 * Created by Administrator on 2016/8/4.
 */
public class Fragment_popup1 extends Fragment {
    private TextView tv_location, tv_destince, tv_addressinfo;

    private PoiInfo poiInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view_popup_frag1 = inflater.inflate(R.layout.framgment_popupwindow, null, false);
        tv_addressinfo = (TextView) view_popup_frag1.findViewById(R.id.id_main_vp_frg_addressinfo);
        tv_location = (TextView) view_popup_frag1.findViewById(R.id.id_main_vp_frg_location);
        tv_destince = (TextView) view_popup_frag1.findViewById(R.id.id_main_vp_frg_destince);
        if (poiInfo != null) {
            setInfo(poiInfo);
        }
        return view_popup_frag1;
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (poiInfo != null) {
//            setInfo(poiInfo);
//        }
    }

    public void setPoiInfo(PoiInfo poiInfo) {
        this.poiInfo = poiInfo;

    }

    public void setInfo(PoiInfo poiInfo) {
        tv_addressinfo.setText(poiInfo.address+"");
//        tv_destince.setText(poiResult.getAllPoi().get(position).);
        // TODO: 2016/8/29 距离数据没找到
        tv_location.setText(poiInfo.name+"");
    }
}
