package com.hkc.listener.routeplan;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.hkc.adapter.RoutePlan_Bus_Adapter;
import com.hkc.mymapy.R;
import com.hkc.mymapy.RouteplanActivity;
import com.hkc.overlay.DrivingRouteOverlay;
import com.hkc.overlay.WalkingRouteOverlay;
import com.hkc.view.DriverRouteLinePlan;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/1.
 */
public class RoutePlanResult implements OnGetRoutePlanResultListener {
    private String TAG = "crazyK";
    private RouteplanActivity routeplanActivity;

    public RoutePlanResult(RouteplanActivity routeplanActivity){
        this.routeplanActivity = routeplanActivity;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
        routeplanActivity.baiduMap.clear();
        routeplanActivity.ll_routePlanContent.removeAllViews();
        routeplanActivity.ll_startDaoHang.removeAllViews();
        if (walkingRouteResult == null || walkingRouteResult.error != SearchResult.ERRORNO.NO_ERROR||walkingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            Toast.makeText(routeplanActivity, "位置坐标不明确，请重新选择位置", Toast.LENGTH_LONG).show();
            if(routeplanActivity.mPoiSearch == null){
                // 初始化搜索模块，注册搜索事件监听
                routeplanActivity. mPoiSearch = PoiSearch.newInstance();
                routeplanActivity. mPoiSearch.setOnGetPoiSearchResultListener(routeplanActivity.poiSearchResult);
                routeplanActivity.mPoiSearch.searchInCity(new PoiCitySearchOption().city(routeplanActivity.currentCity).keyword(routeplanActivity.enNodeStr).pageNum(0));
            }else {
                routeplanActivity.mPoiSearch.searchInCity(new PoiCitySearchOption().city(routeplanActivity.currentCity).keyword(routeplanActivity.enNodeStr).pageNum(0));
            }
        }

        if (walkingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            if (walkingRouteResult.getRouteLines().size() > 1) {
                routeplanActivity.nowResult = walkingRouteResult;
//                Log.i(TAG, "路线个数："+drivingRouteResult.getRouteLines().size());

//                ll_routePlanContent.removeAllViews();
//                ll_startDaoHang.removeAllViews();

                routeplanActivity.ll_routePlanContent.setWeightSum(walkingRouteResult.getRouteLines().size());
                WalkingRouteOverlay overlay = new WalkingRouteOverlay(routeplanActivity.baiduMap);
                routeplanActivity.routeOverlay = overlay;
                routeplanActivity.baiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(walkingRouteResult.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();

                for (int i = 0; i < walkingRouteResult.getRouteLines().size(); i++) {
                    routeplanActivity.routeLine = walkingRouteResult.getRouteLines().get(i);

                    //动态添加路线推荐layout
                    routeplanActivity.driverRouteLinePlan = new DriverRouteLinePlan(routeplanActivity);
                    routeplanActivity.driverRouteLinePlan.setPadding(0,7,0,7);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
                    routeplanActivity.driverRouteLinePlan.setLayoutParams(layoutParams);
                    routeplanActivity.driverRouteLinePlan.setBackgroundColor(Color.WHITE);
                    routeplanActivity.driverRouteLinePlan.setId(i);
                    routeplanActivity.driverRouteLinePlan.setOnClickListener(routeplanActivity.click);
                    routeplanActivity.driverRouteLinePlan.setDistance(routeplanActivity.routeLine.getDistance());
                    routeplanActivity.driverRouteLinePlan.setType("方案"+(i+1));
                    routeplanActivity.driverRouteLinePlan.setTime(routeplanActivity.routeLine.getDuration());
                    routeplanActivity.ll_routePlanContent.addView(routeplanActivity.driverRouteLinePlan);
                }

                Button btn_startDaoHang = new Button(routeplanActivity);
                //蛟神黑科技：将图片变为id资源
                btn_startDaoHang.setBackground(routeplanActivity.getResources().getDrawable(R.mipmap.routeplan_daohang));
                btn_startDaoHang.setId(R.id.button_bin_navi);
                btn_startDaoHang.setOnClickListener(routeplanActivity.click);
                routeplanActivity.ll_startDaoHang.setPadding(10,10,10,10);
                routeplanActivity.ll_startDaoHang.addView(btn_startDaoHang);



            } else if ( walkingRouteResult.getRouteLines().size() == 1 ) {
//                Log.i(TAG, "线路等于1");
                routeplanActivity.routeLine = walkingRouteResult.getRouteLines().get(0);

                WalkingRouteOverlay overlay = new WalkingRouteOverlay(routeplanActivity.baiduMap);
                routeplanActivity.routeOverlay = overlay;
                routeplanActivity.baiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(walkingRouteResult.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();

                Button btn_startDaoHang = new Button(routeplanActivity);
                btn_startDaoHang.setBackground(routeplanActivity.getResources().getDrawable(R.mipmap.routeplan_daohang));
                btn_startDaoHang.setId(R.id.button_bin_navi);
                btn_startDaoHang.setOnClickListener(routeplanActivity.click);
                routeplanActivity.ll_startDaoHang.setPadding(10,10,10,10);
                routeplanActivity.ll_startDaoHang.addView(btn_startDaoHang);
            }

        }

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
        routeplanActivity.baiduMap.clear();
        routeplanActivity.ll_routePlanContent.removeAllViews();
        routeplanActivity.ll_startDaoHang.removeAllViews();

        if (transitRouteResult == null || transitRouteResult.error != SearchResult.ERRORNO.NO_ERROR||transitRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            Toast.makeText(routeplanActivity, "位置坐标不明确，请重新选择位置", Toast.LENGTH_LONG).show();
            if(routeplanActivity.mPoiSearch == null){
                // 初始化搜索模块，注册搜索事件监听
                routeplanActivity.mPoiSearch = PoiSearch.newInstance();
                routeplanActivity.mPoiSearch.setOnGetPoiSearchResultListener(routeplanActivity.poiSearchResult);
                routeplanActivity. mPoiSearch.searchInCity(new PoiCitySearchOption().city(routeplanActivity.currentCity).keyword(routeplanActivity.enNodeStr).pageNum(0));
            }else {
                routeplanActivity.mPoiSearch.searchInCity(new PoiCitySearchOption().city(routeplanActivity.currentCity).keyword(routeplanActivity.enNodeStr).pageNum(0));
            }
        }

        if (transitRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            routeplanActivity.busRouteLineCount = transitRouteResult.getRouteLines().size();
            if (transitRouteResult.getRouteLines().size() > 1) {
                if(routeplanActivity.ll_lv_bus.getVisibility() == View.GONE ){
                    routeplanActivity.ll_lv_bus.setVisibility(View.VISIBLE);
                    routeplanActivity.isItemClick = 1;
                }

                routeplanActivity.nowResult = transitRouteResult;
                /**
                 * 步行906米,到达锦江宾馆站
                 乘坐地铁1号线(升仙湖方向),经过1站,到达天府广场站

                 步行351米,到达南大街站
                 乘坐334路,经过2站,到达东御街站
                 步行228米,到达终点站

                 步行350米,到达南大街站
                 乘坐26路,经过1站,到达人民南路一段站
                 步行350米,到达终点站

                 步行465米,到达文翁路南站
                 乘坐57路(或109路),经过2站,到达东城根南街站
                 步行917米,到达终点站

                 步行857米,到达锦江宾馆站
                 乘坐16路,经过2站,到达天府广场东站
                 步行53米,到达终点站
                 * */
                ArrayList<String> busInfos = new ArrayList<>();

                for(int x = 0 ; x < transitRouteResult.getRouteLines().size() ; x++){
                    StringBuffer stringBuffer = new StringBuffer();
                    for(int y = 0 ; y < transitRouteResult.getRouteLines().get(x).getAllStep().size(); y++){
                        String step = transitRouteResult.getRouteLines().get(x).getAllStep().get(y).getInstructions()+";";
                        stringBuffer.append(step);
//                        Log.i(TAG, "getInstructions(): " +transitRouteResult.getRouteLines().get(x).getAllStep().get(y).getInstructions()+"");
                    }
                    busInfos.add(stringBuffer.toString());
                }

                final RoutePlan_Bus_Adapter routePlan_bus_adapter = new RoutePlan_Bus_Adapter(busInfos,routeplanActivity);
                routeplanActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        routeplanActivity.lv_bus.setAdapter(routePlan_bus_adapter);
                        routeplanActivity.lv_bus.setOnItemClickListener(routeplanActivity);
                    }
                });


//                ll_routePlanContent.setWeightSum(drivingRouteResult.getRouteLines().size());
//                DrivingRouteOverlay overlay = new DrivingRouteOverlay(baiduMap);
//                routeOverlay = overlay;
//                baiduMap.setOnMarkerClickListener(overlay);
//                overlay.setData(drivingRouteResult.getRouteLines().get(0));
//                overlay.addToMap();
//                overlay.zoomToSpan();

//                for (int i = 0; i < drivingRouteResult.getRouteLines().size(); i++) {
//                    routeLine = drivingRouteResult.getRouteLines().get(i);

//                    //动态添加路线推荐layout
//                    driverRouteLinePlan = new DriverRouteLinePlan(this);
//                    driverRouteLinePlan.setPadding(0,7,0,7);
//                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
//                    driverRouteLinePlan.setLayoutParams(layoutParams);
//                    driverRouteLinePlan.setBackgroundColor(Color.WHITE);
//                    driverRouteLinePlan.setId(i);
//                    driverRouteLinePlan.setOnClickListener(this);
//                    driverRouteLinePlan.setDistance(routeLine.getDistance());
//                    driverRouteLinePlan.setType("方案"+(i+1));
//                    driverRouteLinePlan.setTime(routeLine.getDuration());
//                    ll_routePlanContent.addView(driverRouteLinePlan);
//                }

//                Button btn_startDaoHang = new Button(this);
//                btn_startDaoHang.setBackground(getResources().getDrawable(R.mipmap.routeplan_daohang));
//                btn_startDaoHang.setId(R.id.button_bin_navi);
//                btn_startDaoHang.setOnClickListener(this);
//                ll_startDaoHang.setPadding(10,10,10,10);
//                ll_startDaoHang.addView(btn_startDaoHang);



            } else if ( transitRouteResult.getRouteLines().size() == 1 ) {
                Log.i(TAG, "公交查询结果只有一个");
                if (routeplanActivity.ll_lv_bus.getVisibility() == View.GONE){
                    routeplanActivity.isItemClick = 0 ;
                }
//                Log.i(TAG, "线路等于1");
//                routeLine = drivingRouteResult.getRouteLines().get(0);
//
//                DrivingRouteOverlay overlay = new DrivingRouteOverlay(baiduMap);
//                routeOverlay = overlay;
//                baiduMap.setOnMarkerClickListener(overlay);
//                overlay.setData(drivingRouteResult.getRouteLines().get(0));
//                overlay.addToMap();
//                overlay.zoomToSpan();
//
//                Button btn_startDaoHang = new Button(this);
//                btn_startDaoHang.setBackground(getResources().getDrawable(R.mipmap.routeplan_daohang));
//                btn_startDaoHang.setId(R.id.button_bin_navi);
//                btn_startDaoHang.setOnClickListener(this);
//                ll_startDaoHang.setPadding(10,10,10,10);
//                ll_startDaoHang.addView(btn_startDaoHang);
            }

        }
//        Log.i(TAG, "transitRouteResult: " +transitRouteResult.toString());
////        Log.i(TAG, "getStarting().getTitle(): " +transitRouteResult.getRouteLines().get(0).getStarting().getTitle());//神马都没有
////        Log.i(TAG, "getVehicleInfo().getTitle(): " +transitRouteResult.getRouteLines().get(0).getAllStep().get(0).getVehicleInfo().getTitle()+"" );  //空
////        Log.i(TAG, "getVehicleInfo().getPassStationNum(): " +transitRouteResult.getRouteLines().get(0).getAllStep().get(0).getVehicleInfo().getPassStationNum()+"");//空
////        Log.i(TAG, "getVehicleInfo().getTotalPrice(): " +transitRouteResult.getRouteLines().get(0).getAllStep().get(0).getVehicleInfo().getTotalPrice()+"");//空
////        Log.i(TAG, "getVehicleInfo().getZonePrice(): " +transitRouteResult.getRouteLines().get(0).getAllStep().get(0).getVehicleInfo().getZonePrice()+"");//空
//        Log.i(TAG, "getTitle(): " +transitRouteResult.getRouteLines().get(0).getTitle()+"");//null
//        Log.i(TAG, "getStarting().getTitle(): " +transitRouteResult.getRouteLines().get(0).getStarting().getTitle()+"");//神马都没有
//        for(int x = 0 ; x < transitRouteResult.getRouteLines().size() ; x++){
//            for(int y = 0 ; y < transitRouteResult.getRouteLines().get(x).getAllStep().size(); y++){
//                Log.i(TAG, "getInstructions(): " +transitRouteResult.getRouteLines().get(x).getAllStep().get(y).getInstructions()+"");
//            }
//        }
//        Log.i(TAG, "getStepType: "+transitRouteResult.getRouteLines().get(0).getAllStep().get(1).getStepType().name()+"");





    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        routeplanActivity.baiduMap.clear();
        routeplanActivity.ll_routePlanContent.removeAllViews();
        routeplanActivity.ll_startDaoHang.removeAllViews();
//        Log.i(TAG, "onGetDrivingRouteResult: ");
        if (drivingRouteResult == null || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR||drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            Toast.makeText(routeplanActivity, "位置坐标不明确，请重新选择位置", Toast.LENGTH_LONG).show();
            if(routeplanActivity.mPoiSearch == null){
                // 初始化搜索模块，注册搜索事件监听
                routeplanActivity. mPoiSearch = PoiSearch.newInstance();
                routeplanActivity. mPoiSearch.setOnGetPoiSearchResultListener(routeplanActivity.poiSearchResult);
                routeplanActivity. mPoiSearch.searchInCity(new PoiCitySearchOption().city(routeplanActivity.currentCity).keyword(routeplanActivity.enNodeStr).pageNum(0));
            }else {
                routeplanActivity. mPoiSearch.searchInCity(new PoiCitySearchOption().city(routeplanActivity.currentCity).keyword(routeplanActivity.enNodeStr).pageNum(0));
            }
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            if (drivingRouteResult.getRouteLines().size() > 1) {
                routeplanActivity.nowResult = drivingRouteResult;

                routeplanActivity.ll_routePlanContent.setWeightSum(drivingRouteResult.getRouteLines().size());
                DrivingRouteOverlay overlay = new DrivingRouteOverlay(routeplanActivity.baiduMap);
                routeplanActivity.routeOverlay = overlay;
                routeplanActivity.baiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(drivingRouteResult.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();

                for (int i = 0; i < drivingRouteResult.getRouteLines().size(); i++) {
                    routeplanActivity.routeLine = drivingRouteResult.getRouteLines().get(i);

                    //动态添加路线推荐layout
                    routeplanActivity.driverRouteLinePlan = new DriverRouteLinePlan(routeplanActivity);
                    routeplanActivity.driverRouteLinePlan.setPadding(0,7,0,7);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
                    routeplanActivity.driverRouteLinePlan.setLayoutParams(layoutParams);
                    routeplanActivity.driverRouteLinePlan.setBackgroundColor(Color.WHITE);
                    routeplanActivity.driverRouteLinePlan.setId(i);
                    routeplanActivity.driverRouteLinePlan.setOnClickListener(routeplanActivity.click);
                    routeplanActivity.driverRouteLinePlan.setDistance(routeplanActivity.routeLine.getDistance());
                    routeplanActivity.driverRouteLinePlan.setType("方案"+(i+1));
                    routeplanActivity.driverRouteLinePlan.setTime(routeplanActivity.routeLine.getDuration());
                    routeplanActivity.ll_routePlanContent.addView(routeplanActivity.driverRouteLinePlan);
                }

                Button btn_startDaoHang = new Button(routeplanActivity);
                btn_startDaoHang.setBackground(routeplanActivity.getResources().getDrawable(R.mipmap.routeplan_daohang));
                btn_startDaoHang.setId(R.id.button_bin_navi);
                btn_startDaoHang.setOnClickListener(routeplanActivity.click);
                routeplanActivity.ll_startDaoHang.setPadding(10,10,10,10);
                routeplanActivity.ll_startDaoHang.addView(btn_startDaoHang);



            } else if ( drivingRouteResult.getRouteLines().size() == 1 ) {
//                Log.i(TAG, "线路等于1");
                routeplanActivity.routeLine = drivingRouteResult.getRouteLines().get(0);

                DrivingRouteOverlay overlay = new DrivingRouteOverlay(routeplanActivity.baiduMap);
                routeplanActivity.routeOverlay = overlay;
                routeplanActivity. baiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(drivingRouteResult.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();

                Button btn_startDaoHang = new Button(routeplanActivity);
                btn_startDaoHang.setBackground(routeplanActivity.getResources().getDrawable(R.mipmap.routeplan_daohang));
                btn_startDaoHang.setId(R.id.button_bin_navi);
                btn_startDaoHang.setOnClickListener(routeplanActivity.click);
                routeplanActivity.ll_startDaoHang.setPadding(10,10,10,10);
                routeplanActivity.ll_startDaoHang.addView(btn_startDaoHang);
            }

        }
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
        Log.i(TAG, "onGetBikingRouteResult: ");
    }
}
