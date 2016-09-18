package com.hkc.listener.routeplan;


import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.hkc.mymapy.R;
import com.hkc.mymapy.RouteplanActivity;
import com.hkc.overlay.DrivingRouteOverlay;
import com.hkc.overlay.WalkingRouteOverlay;
import com.hkc.utitls.ScreenUtils;

/**
 * Created by Administrator on 2016/9/1.
 */
public class Click implements View.OnClickListener {
    private String TAG = "crazyK";

    private RouteplanActivity context;
    public Click(RouteplanActivity context){
        this.context = context;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //标准地图
            case R.id.id_main_tv_changemap_biaozhun:
                context.tv_changemap_biaozhun.setSelected(true);
                context.tv_changemap_weixing.setSelected(false);
                context.tv_changemap_gongjiao.setSelected(false);
                context.baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                context.popupWindow_changemap.dismiss();
                break;
            //卫星地图
            case R.id.id_main_tv_changemap_weixing:
                context.tv_changemap_biaozhun.setSelected(false);
                context.tv_changemap_weixing.setSelected(true);
                context.tv_changemap_gongjiao.setSelected(false);
                context.baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                context.popupWindow_changemap.dismiss();
                break;
            //公交地图
            case R.id.id_main_tv_changemap_gongjiao:
                context.tv_changemap_biaozhun.setSelected(false);
                context.tv_changemap_weixing.setSelected(false);
                context.tv_changemap_gongjiao.setSelected(true);
                break;
            //改变地图按钮
            case R.id.id_routeplan_changemap:
                if (!context.popupWindow_changemap.isShowing()) {
                    int position[] = new int[2];
                    v.getLocationInWindow(position);
                    context.popupWindow_changemap.showAtLocation(v, Gravity.RIGHT | Gravity.TOP, ScreenUtils.screenWidth(context) - position[0] - v.getWidth(), position[1] + v.getHeight());
                } else {
                    context.popupWindow_changemap.dismiss();
                }
                break;
            //路况按钮
            case R.id.id_routeplan_lukuang:
                if (context.count_lukuang++ % 2 == 0) {
                    context.tv_lukuang.setSelected(true);
                    context.baiduMap.setTrafficEnabled(true);
                } else {
                    context.tv_lukuang.setSelected(false);
                    context.baiduMap.setTrafficEnabled(false);
                }
                break;
            //发现(回归原点 并切换模式)
            case R.id.id_routeplan_find:
                context.locationMode = MyLocationConfiguration.LocationMode.NORMAL;
                context.centerToMyLocation(context.currentLatLng);
//                Log.i(TAG, "currentLatLng: "+currentLatLng);
                break;
            //路线计划1
            case 0:
                if(context.iv_car.isSelected()){
                    DrivingRouteResult drivingRouteResult = (DrivingRouteResult) context.nowResult;
                    DrivingRouteOverlay overlay0 = new DrivingRouteOverlay(context.baiduMap);
                    context.routeOverlay = overlay0;
                    context.baiduMap.setOnMarkerClickListener(overlay0);
                    overlay0.setData(drivingRouteResult.getRouteLines().get(0));
                    context.baiduMap.clear();
                    overlay0.addToMap();
                    overlay0.zoomToSpan();
                }
                if(context.iv_walk.isSelected()){
                    WalkingRouteResult walkingRouteResult = (WalkingRouteResult) context.nowResult;
                    WalkingRouteOverlay overlay0 = new WalkingRouteOverlay(context.baiduMap);
                    context.routeOverlay = overlay0;
                    context.baiduMap.setOnMarkerClickListener(overlay0);
                    overlay0.setData(walkingRouteResult.getRouteLines().get(0));
                    context.baiduMap.clear();
                    overlay0.addToMap();
                    overlay0.zoomToSpan();
                }

                break;
            //路线计划2
            case 1:
                if(context.iv_car.isSelected()){
                    DrivingRouteResult drivingRouteResult = (DrivingRouteResult) context.nowResult;
                    DrivingRouteOverlay overlay1 = new DrivingRouteOverlay(context.baiduMap);
                    context.routeOverlay = overlay1;
                    context.baiduMap.setOnMarkerClickListener(overlay1);
                    overlay1.setData(drivingRouteResult.getRouteLines().get(1));
                    context. baiduMap.clear();
                    overlay1.addToMap();
                    overlay1.zoomToSpan();
                }
                if(context.iv_walk.isSelected()){
                    WalkingRouteResult walkingRouteResult = (WalkingRouteResult) context.nowResult;
                    WalkingRouteOverlay overlay0 = new WalkingRouteOverlay(context.baiduMap);
                    context.routeOverlay = overlay0;
                    context.baiduMap.setOnMarkerClickListener(overlay0);
                    overlay0.setData(walkingRouteResult.getRouteLines().get(1));
                    context.baiduMap.clear();
                    overlay0.addToMap();
                    overlay0.zoomToSpan();
                }

                break;
            //路线计划3
            case 2:
                if(context.iv_car.isSelected()){
                    DrivingRouteResult drivingRouteResult = (DrivingRouteResult) context.nowResult;
                    DrivingRouteOverlay overlay2 = new DrivingRouteOverlay(context.baiduMap);
                    context.routeOverlay = overlay2;
                    context.baiduMap.setOnMarkerClickListener(overlay2);
                    overlay2.setData(drivingRouteResult.getRouteLines().get(2));
                    context.baiduMap.clear();
                    overlay2.addToMap();
                    overlay2.zoomToSpan();
                }
                if(context.iv_walk.isSelected()){
                    WalkingRouteResult walkingRouteResult = (WalkingRouteResult) context.nowResult;
                    WalkingRouteOverlay overlay0 = new WalkingRouteOverlay(context.baiduMap);
                    context.routeOverlay = overlay0;
                    context.baiduMap.setOnMarkerClickListener(overlay0);
                    overlay0.setData(walkingRouteResult.getRouteLines().get(2));
                    context.baiduMap.clear();
                    overlay0.addToMap();
                    overlay0.zoomToSpan();
                }

                break;
            //poi搜索后导航按钮被点击
            case R.id.id_routeplan_popupwindow_marker_iv_daohang:
                PlanNode stNode_Poi = PlanNode.withLocation(context.currentLatLng);
                PlanNode enNode_Poi = PlanNode.withLocation(context.endLatLng);
                if(context.iv_car.isSelected()){
                    context.routePlanSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode_Poi).to(enNode_Poi));
                }else if(context.iv_bus.isSelected()){
                    context.routePlanSearch.transitSearch((new TransitRoutePlanOption()).from(stNode_Poi).city(context.currentCity).to(enNode_Poi));
                }else if(context.iv_walk.isSelected()){
                    context.routePlanSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode_Poi).to(enNode_Poi));
                }
                if(context.rl_vp.getVisibility() == View.VISIBLE){
                    context.rl_vp.setVisibility(View.GONE);
                }
                break;
            //开始导航按钮被点击
            case R.id.button_bin_navi:
                Toast.makeText(context, "点击开始导航", Toast.LENGTH_SHORT).show();
                break;
            //iv_car驾车图标被点击
            case R.id.id_routeplan_way_car:
                if(context.ll_lv_bus.getVisibility() == View.VISIBLE){
                    context.ll_lv_bus.setVisibility(View.GONE);
                    context.isItemClick = 0;
                }
                if(context.rl_vp.getVisibility() == View.VISIBLE){
                    context.rl_vp.setVisibility(View.GONE);
                }
                if(context.iv_car.isSelected()){
                    return;
                }else {
                    context.iv_car.setSelected(true);
                    context.iv_bus.setSelected(false);
                    context.iv_walk.setSelected(false);

                    if(context.flag == 0 ){
                        PlanNode stNode = PlanNode.withLocation(context.currentLatLng);
                        context.routePlanSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(context.enNode));
                    }else if(context.flag == 1){
                        if(context.stNodeStr == null){
                            Toast.makeText(context, "开始地点stNode为空", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            PlanNode stNode = PlanNode.withCityNameAndPlaceName(context.currentCity,context.enNodeStr);
                            context.routePlanSearch.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(context.enNode));
                        }
                    }
                }
                break;
            //iv_bus公交图标被点击：
            case R.id.id_routeplan_way_bus:
                if(context.rl_vp.getVisibility() == View.VISIBLE){
                    context.rl_vp.setVisibility(View.GONE);
                    context.isItemClick = 0;
                }
                if(context.iv_bus.isSelected()){
                    return;
                }else {
                    context.iv_car.setSelected(false);
                    context.iv_bus.setSelected(true);
                    context.iv_walk.setSelected(false);

                    if(context.flag == 0 ){
                        PlanNode stNode = PlanNode.withLocation(context.currentLatLng);
                        context.routePlanSearch.transitSearch((new TransitRoutePlanOption()).from(stNode).city(context.currentCity).to(context.enNode));
                    }else if(context.flag == 1){
                        if(context.stNodeStr == null){
                            Toast.makeText(context, "开始地点stNode为空", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            PlanNode stNode = PlanNode.withCityNameAndPlaceName(context.currentCity,context.enNodeStr);
                            context.routePlanSearch.transitSearch(new TransitRoutePlanOption().from(stNode).city(context.currentCity).to(context.enNode));
                        }
                    }
                }
                break;
            //iv_walk步行图标被点击：
            case R.id.id_routeplan_way_walk:
                if(context.ll_lv_bus.getVisibility() == View.VISIBLE){
                    context.ll_lv_bus.setVisibility(View.GONE);
                    context.isItemClick = 0;
                }
                if(context.rl_vp.getVisibility() == View.VISIBLE){
                    context. rl_vp.setVisibility(View.GONE);
                }
                if(context.iv_walk.isSelected()){
                    return;
                }else {
                    context.iv_car.setSelected(false);
                    context. iv_bus.setSelected(false);
                    context.iv_walk.setSelected(true);

                    if(context.flag == 0 ){
                        PlanNode stNode = PlanNode.withLocation(context.currentLatLng);
                        context.routePlanSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(context.enNode));
                    }else if(context.flag == 1){
                        if(context.stNodeStr == null){
                            Toast.makeText(context, "开始地点stNode为空", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            PlanNode stNode = PlanNode.withCityNameAndPlaceName(context.currentCity,context.enNodeStr);
                            context.routePlanSearch.walkingSearch(new WalkingRoutePlanOption().from(stNode).to(context.enNode));
                        }
                    }
                }
                break;
            //返回按钮
            case R.id.id_routeplan_back:
                if(context.iv_bus.isSelected() && context.isItemClick == 0 && context.busRouteLineCount > 1){
                    if(context.ll_lv_bus.getVisibility() == View.GONE){
                        context.ll_lv_bus.setVisibility(View.VISIBLE);
                        context.isItemClick = 1;
                    }
                }else {
                    context.finish();
                }
                break;
        }
    }
}
