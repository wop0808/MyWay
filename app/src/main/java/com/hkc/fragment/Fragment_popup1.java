package com.hkc.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiResult;
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
        View view_popup_frag1 = inflater.inflate(R.layout.framgment_popupwindow, container, false);
        tv_addressinfo = (TextView) view_popup_frag1.findViewById(R.id.id_main_vp_frg_addressinfo);
        tv_location = (TextView) view_popup_frag1.findViewById(R.id.id_main_vp_frg_location);
        tv_destince = (TextView) view_popup_frag1.findViewById(R.id.id_main_vp_frg_destince);
        if (poiInfo != null) {
            setInfo(poiInfo);
        }
        return view_popup_frag1;
    }

    public void setPoiInfo(PoiInfo poiInfo) {
        this.poiInfo = poiInfo;
    }

    public void setInfo(PoiInfo poiInfo) {
        tv_addressinfo.setText(poiInfo.address+"");
//        tv_destince.setText(poiResult.getAllPoi().get(position).);
        tv_location.setText(poiInfo.name+"");
    }
}
