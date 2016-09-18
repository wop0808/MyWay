package com.hkc.listener.routeplan;

import android.app.ProgressDialog;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.hkc.adapter.Vp_AddressInfo_Adapter;
import com.hkc.mymapy.R;
import com.hkc.mymapy.RouteplanActivity;
import com.hkc.overlay.PoiOverlay;

/**
 * Created by Administrator on 2016/9/1.
 */
public class PoiSearchResult implements OnGetPoiSearchResultListener {
    private RouteplanActivity routeplanActivity;

    public PoiSearchResult(RouteplanActivity routeplanActivity){
        this.routeplanActivity = routeplanActivity;
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        ProgressDialog progressDialog = ProgressDialog.show(routeplanActivity, null, "数据加载中");
        routeplanActivity.poiResult = poiResult;
        routeplanActivity. baiduMap.clear();
        PoiOverlay overlay_FromSearch = new MyPoiOverlay(routeplanActivity.baiduMap);
        routeplanActivity.baiduMap.setOnMarkerClickListener(overlay_FromSearch);
        overlay_FromSearch.setData(poiResult);
        overlay_FromSearch.addToMap();
        overlay_FromSearch.zoomToSpan();

        //蛟神黑科技 将activityi获得的数据-->adapter -->fragment
        //避免了控件在onCreateView方法还未执行完时，对控件进行操作，爆出空指针异常
        if (routeplanActivity.vp_addressInfo.getAdapter() == null) {
            routeplanActivity. vp_addressInfo_adapter = new Vp_AddressInfo_Adapter(routeplanActivity.fragmentManager,progressDialog);
            routeplanActivity.vp_addressInfo.setAdapter(routeplanActivity.vp_addressInfo_adapter);
        }
        routeplanActivity.vp_addressInfo_adapter.setPoiResult(poiResult);
    }


    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    //工具类PoiOverlay的自定义子类
    public class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        //点击查询结果覆盖物的监听
        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);

            //判断点击poi图片后圆形导航按钮的样子
            if(routeplanActivity.iv_car.isSelected()){
                routeplanActivity.iv_daohang.setImageResource(R.mipmap.popupwindow_car_daohang);
            }else if(routeplanActivity.iv_bus.isSelected()){
                routeplanActivity.iv_daohang.setImageResource(R.mipmap.popupwindow_daohang);
            }else if(routeplanActivity.iv_walk.isSelected()){
                routeplanActivity.iv_daohang.setImageResource(R.mipmap.popupwindow_walk_daohang);
            }


            routeplanActivity.poiInfo_FromMarkerAndVp = getPoiResult().getAllPoi().get(index);
            //获得终点坐标，导航用
            routeplanActivity.endLatLng = routeplanActivity.poiInfo_FromMarkerAndVp.location;

            if (routeplanActivity.mPoiSearch == null){
                routeplanActivity. mPoiSearch = PoiSearch.newInstance();
            }
            routeplanActivity.centerToMyLocation(routeplanActivity.poiInfo_FromMarkerAndVp.location);

            if(routeplanActivity.rl_vp.getVisibility() == View.GONE){
                routeplanActivity.rl_vp.setVisibility(View.VISIBLE);
            }

            routeplanActivity.mapView.showZoomControls(false);

            routeplanActivity.vp_addressInfo.setCurrentItem(index, true);

            routeplanActivity. mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(routeplanActivity.poiInfo_FromMarkerAndVp.uid));
            // }
            return true;
        }
    }
}
