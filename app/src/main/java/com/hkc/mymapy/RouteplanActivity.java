package com.hkc.mymapy;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.hkc.adapter.RoutePlan_Bus_Adapter;
import com.hkc.adapter.Vp_AddressInfo_Adapter;
import com.hkc.handler.Handler_Route_CurrentLatLng;
import com.hkc.listener.MyOritationListener;
import com.hkc.listener.OnGetCurrentLalngListener;
import com.hkc.listener.routeplan.Click;
import com.hkc.listener.routeplan.PoiSearchResult;
import com.hkc.listener.routeplan.RoutePlanResult;
import com.hkc.overlay.DrivingRouteOverlay;
import com.hkc.overlay.OverlayManager;
import com.hkc.overlay.PoiOverlay;
import com.hkc.overlay.TransitRouteOverlay;
import com.hkc.overlay.WalkingRouteOverlay;
import com.hkc.view.DriverRouteLinePlan;

import java.util.ArrayList;

/**
 * DrivingPolicy出行规划
 * ECAR_AVOID_JAM
 驾车策略： 躲避拥堵
 ECAR_DIS_FIRST
 驾乘检索策略常量：最短距离
 ECAR_FEE_FIRST
 驾乘检索策略常量：较少费用
 ECAR_TIME_FIRST
 驾乘检索策略常量：时间优先

 * RouteLine获取耗时、路线长度、路线名称
 */

public class RouteplanActivity extends AppCompatActivity implements   OnGetCurrentLalngListener, ViewPager.OnPageChangeListener, AdapterView.OnItemClickListener {
    private final String TAG = "crazyK";
    //路况按钮
    public TextView tv_lukuang;
    public ImageView iv_find;
    public View v_popupwindow;
    public int count_lukuang = 0;
    //返回按钮
    public LinearLayout ll_back;
    //MapView
    public MapView mapView;
    public BaiduMap baiduMap;
    //弹窗
    public PopupWindow popupWindow_changemap;
    //改变窗口btn 弹出的三个控件
    public TextView tv_changemap_biaozhun, tv_changemap_weixing, tv_changemap_gongjiao;
    //改变窗口的按钮
    public ImageView iv_changemap;
    //fragmentManager
    static public FragmentManager fragmentManager;
    //定位
    public LocationClient locationClient;
    public MyLocationListener myLocationListener;
    //当前位置
    public LatLng currentLatLng;
    public String currentCity;
    //模式切换
    public MyLocationConfiguration.LocationMode locationMode;
    //当前位置
    public float currentX;
    //自定义定位图标
    public BitmapDescriptor iconLocation;
    //自定义方向传感器监听
    public MyOritationListener myOritationListener;
    //从 way界面传来的intent相关
    public int Mode_Search = -1;
    public int flag = -1;
    public String stNodeStr;
    public String enNodeStr;

    //导航栏三个出行方式 控件
    public ImageView iv_car, iv_bus, iv_walk;
    //路线计划搜索相关
    public RoutePlanSearch routePlanSearch;
    //搜索结果中间变量
    public SearchResult nowResult;
    //路线、覆盖物相关
    public RouteLine routeLine = null;
    public OverlayManager routeOverlay = null;
    //下方动态添加控件相关
    public LinearLayout ll_routePlanContent;
    public LinearLayout ll_startDaoHang;
    public DriverRouteLinePlan driverRouteLinePlan;
    //当搜索结果有问题时使用的poi搜索及相关
    public RelativeLayout rl_vp;
    public ViewPager vp_addressInfo;
    public Vp_AddressInfo_Adapter vp_addressInfo_adapter;
    public PoiSearch mPoiSearch;
    public PoiResult poiResult;
    public ImageView iv_daohang;
    //点击搜索结果marker 以及 滑动viewpager时获得的poiInfo
    public PoiInfo poiInfo_FromMarkerAndVp;
    //poi搜索后获得的终点坐标
    public LatLng endLatLng;
    //神奇的ProgressDialog
    ProgressDialog progressDialog;
    //由上个way界面传过来的终点Str 通过PlanNode.withCityNameAndPlaceName(bdLocation.getCity(),this.enNodeStr)转化为的终点地址
    public PlanNode enNode;
    //公交搜索后的信息listview相关
    public LinearLayout ll_lv_bus;
    public ListView lv_bus;
    //用于判断公交搜索后的listview是否显示，以及改写返回键的逻辑
    public int isItemClick = 0;
    public int busRouteLineCount = 0;
    //RouteplanActivity的监听类
    public Click click;
    public RoutePlanResult routePlanResult;
    public PoiSearchResult poiSearchResult;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routeplan);


        click = new Click(this);
        routePlanResult = new RoutePlanResult(this);
        poiSearchResult = new PoiSearchResult(this);

        progressDialog = ProgressDialog.show(this,null,"数据加载中");

        Intent intent_RouteFromWay = getIntent();
        Mode_Search = intent_RouteFromWay.getIntExtra("MODE_search", 0);
        flag = intent_RouteFromWay.getIntExtra("flag", -1);
        stNodeStr = intent_RouteFromWay.getStringExtra("stNodeStr");
        enNodeStr = intent_RouteFromWay.getStringExtra("enNodeStr");

        // 初始化搜索模块，注册事件监听
        routePlanSearch = RoutePlanSearch.newInstance();
        routePlanSearch.setOnGetRoutePlanResultListener(routePlanResult);

        initView();
        initListener();
        initMapRelate();


    }



    public void initView() {
        //获取地图控件引用
        mapView = (MapView) findViewById(R.id.id_routeplan_mapview);
        baiduMap = mapView.getMap();
        //地图内控件
        tv_lukuang = (TextView) findViewById(R.id.id_routeplan_lukuang);
        iv_find = (ImageView) findViewById(R.id.id_routeplan_find);
        iv_changemap = (ImageView) findViewById(R.id.id_routeplan_changemap);
        //获取fragment Manager
        fragmentManager = this.getSupportFragmentManager();
        //设置地图为普通地图
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        initPopWindow();
        tv_changemap_biaozhun.setSelected(true);
        //导航栏三个出行方式 控件
        iv_car = (ImageView) findViewById(R.id.id_routeplan_way_car);
        iv_bus = (ImageView) findViewById(R.id.id_routeplan_way_bus);
        iv_walk = (ImageView) findViewById(R.id.id_routeplan_way_walk);

        ll_routePlanContent = (LinearLayout) findViewById(R.id.id_routeplan_ll_typle_daohang);
        ll_startDaoHang = (LinearLayout) findViewById(R.id.id_routeplan_startdaohang);

        rl_vp = (RelativeLayout) findViewById(R.id.id_routeplan_rl_vp);
        vp_addressInfo = (ViewPager) findViewById(R.id.id_routeplan_viewpager_addressinfo);
        iv_daohang = (ImageView) findViewById(R.id.id_routeplan_popupwindow_marker_iv_daohang);

        ll_back = (LinearLayout) findViewById(R.id.id_routeplan_back);

        ll_lv_bus = (LinearLayout) findViewById(R.id.id_routeplan_ll_lv_bussearch);
        lv_bus = (ListView) findViewById(R.id.id_routeplan_lv);
    }

    public void initPopWindow() {
        //创建popwindow
        v_popupwindow = View.inflate(this, R.layout.popwindow_main_changemap, null);
        popupWindow_changemap = new PopupWindow(v_popupwindow, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow_changemap.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow_changemap.setFocusable(false);
        popupWindow_changemap.setOutsideTouchable(true);

        //找弹窗内的控件：
        tv_changemap_biaozhun = (TextView) v_popupwindow.findViewById(R.id.id_main_tv_changemap_biaozhun);
        tv_changemap_weixing = (TextView) v_popupwindow.findViewById(R.id.id_main_tv_changemap_weixing);
        tv_changemap_gongjiao = (TextView) v_popupwindow.findViewById(R.id.id_main_tv_changemap_gongjiao);
    }

    public void initListener() {
        //设置监听
        iv_changemap.setOnClickListener(click);
        tv_changemap_biaozhun.setOnClickListener(click);
        tv_changemap_weixing.setOnClickListener(click);
        tv_changemap_gongjiao.setOnClickListener(click);
        tv_lukuang.setOnClickListener(click);
        iv_find.setOnClickListener(click);
        //导航栏三个出行方式 控件
        iv_car.setOnClickListener(click);
        iv_bus.setOnClickListener(click);
        iv_walk.setOnClickListener(click);

        iv_daohang.setOnClickListener(click);

        vp_addressInfo.setOnPageChangeListener(this);

        ll_back.setOnClickListener(click);
    }

    //回到中心位置
    public void centerToMyLocation(LatLng latLng) {
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(mapStatusUpdate);
    }

    //初始化 地图定位相关 ，方向传感器
    public void initMapRelate() {
        //判断导航方式
        whichWay(this.Mode_Search);

        locationMode = MyLocationConfiguration.LocationMode.NORMAL;
        locationClient = new LocationClient(this);
        myLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(myLocationListener);

        LocationClientOption locationClientOption = new LocationClientOption();
        locationClientOption.setCoorType("bd09ll");//设置坐标类型
        locationClientOption.setIsNeedAddress(true);
        locationClientOption.setOpenGps(true);

        locationClient.setLocOption(locationClientOption);
        //初始化自定义图标
        iconLocation = BitmapDescriptorFactory.fromResource(R.mipmap.logo_direction_l);
        //方向传感器接口
        myOritationListener = new MyOritationListener(this);
        myOritationListener.setOnOritationListener(new MyOritationListener.OnOritationListener() {
            @Override
            public void onOritationChanged(float x) {
                currentX = x;
            }
        });



    }


    //判断出行方式，并且设置上方导航栏的select属性
    public void whichWay(int Mode_Search) {
        switch (Mode_Search) {
            //car出行
            case 1:
                iv_car.setSelected(true);
                iv_bus.setSelected(false);
                iv_walk.setSelected(false);
                iv_daohang.setImageResource(R.mipmap.popupwindow_car_daohang);
                break;
            case 2:
                iv_car.setSelected(false);
                iv_bus.setSelected(true);
                iv_walk.setSelected(false);
                iv_daohang.setImageResource(R.mipmap.popupwindow_daohang);
                break;
            case 3:
                iv_car.setSelected(false);
                iv_bus.setSelected(false);
                iv_walk.setSelected(true);
                iv_daohang.setImageResource(R.mipmap.popupwindow_walk_daohang);
                break;
            default:
                Log.i(TAG, "出行方式Mode_Search错误");
                break;
        }
    }

    //接口回调 获得currentLatLng后，开始路线查询
    //在定位相关中 获取currentLatlng后调用
    //判断出行方式MODE_search，出发地址flag、stNode，终止地址enNode
    //三种出行状态MODE_search: car：1 bus：2 walk：3 ,出发地的标志flag，0为我的位置，1为其他位置
    //flag = 0 时，stNode为null；flag = 1时 ，stNode为地址名称
    /**
     * 代码逻辑仍然有问题，不能支持自动全国搜索
     */
    @Override
    public void startSearch(BDLocation bdLocation) {
        this.currentLatLng = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
        Log.i(TAG,bdLocation.getLatitude()+"" );

        //获得终点enNode
        enNode = PlanNode.withCityNameAndPlaceName(bdLocation.getCity(),this.enNodeStr);

        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }

        //car出行
        if(iv_car.isSelected()){
            if(flag == 0 ){
                PlanNode stNode = PlanNode.withLocation(currentLatLng);
                routePlanSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
            }else if(flag == 1){
                if(stNodeStr == null){
                    Toast.makeText(RouteplanActivity.this, "开始地点stNode为空", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    PlanNode stNode = PlanNode.withCityNameAndPlaceName(bdLocation.getCity(),enNodeStr);
                    routePlanSearch.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(enNode));
                }
            }

        }else if(iv_bus.isSelected()){//bus出行
            if(flag == 0 ){
                PlanNode stNode = PlanNode.withLocation(currentLatLng);
                routePlanSearch.transitSearch((new TransitRoutePlanOption()).from(stNode).city(bdLocation.getCity()).to(enNode));
            }else if(flag == 1){
                if(stNodeStr == null){
                    Toast.makeText(RouteplanActivity.this, "开始地点stNode为空", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    PlanNode stNode = PlanNode.withCityNameAndPlaceName(bdLocation.getCity(),enNodeStr);
                    routePlanSearch.transitSearch(new TransitRoutePlanOption().from(stNode).city(bdLocation.getCity()).to(enNode));
                }
            }

        }else if(iv_walk.isSelected()){//步行
            if(flag == 0 ){
                PlanNode stNode = PlanNode.withLocation(currentLatLng);
                routePlanSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(enNode));
            }else if(flag == 1){
                if(stNodeStr == null){
                    Toast.makeText(RouteplanActivity.this, "开始地点stNode为空", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    PlanNode stNode = PlanNode.withCityNameAndPlaceName(bdLocation.getCity(),enNodeStr);
                    routePlanSearch.walkingSearch(new WalkingRoutePlanOption().from(stNode).to(enNode));
                }
            }
        }
    }



    //viewpager的三个监听回调
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        poiInfo_FromMarkerAndVp = poiResult.getAllPoi().get(position);
//        endLatLng = poiInfo_FromMarkerAndVp.location;
        centerToMyLocation(poiInfo_FromMarkerAndVp.location);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(isItemClick == 0 && iv_bus.isSelected()){
                moveTaskToBack(false);
                if(ll_lv_bus.getVisibility() == View.GONE){
                    ll_lv_bus.setVisibility(View.VISIBLE);
                    isItemClick = 1 ;
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK ){
            if(isItemClick == 0 && iv_bus.isSelected()){
                moveTaskToBack(false);
                if(ll_lv_bus.getVisibility() == View.GONE){
                    ll_lv_bus.setVisibility(View.VISIBLE);
                    isItemClick = 1 ;
                    return true;
                }
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    //公交信息查询后的listview的点击监听
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        baiduMap.clear();
        if(iv_bus.isSelected()){
            TransitRouteResult transitRouteResult = (TransitRouteResult) this.nowResult;

            if(ll_lv_bus.getVisibility() == View.VISIBLE){
                ll_lv_bus.setVisibility(View.GONE);
                isItemClick = 0;
            }

            TransitRouteOverlay overlay = new TransitRouteOverlay(baiduMap);
            baiduMap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(transitRouteResult.getRouteLines().get(position));
            overlay.addToMap();
            overlay.zoomToSpan();

        }
    }


    private class MyLocationListener implements BDLocationListener {
        private Handler_Route_CurrentLatLng handler_Route_CurrentLatLng;

        public MyLocationListener() {
            handler_Route_CurrentLatLng = new Handler_Route_CurrentLatLng(RouteplanActivity.this);
        }

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            MyLocationData myLocationData = new MyLocationData.Builder()
                    .direction(currentX)
                    .accuracy(bdLocation.getRadius())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();

            baiduMap.setMyLocationData(myLocationData);

            currentCity = bdLocation.getCity();
            //设置自定义图标
            MyLocationConfiguration configuration = new MyLocationConfiguration(locationMode, true, iconLocation);
            baiduMap.setMyLocationConfigeration(configuration);

            //将currentLatLng传给主线程
            Message message = handler_Route_CurrentLatLng.obtainMessage(1);
            message.obj = bdLocation;
            handler_Route_CurrentLatLng.sendMessage(message);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //开启定位
        baiduMap.setMyLocationEnabled(true);
        if (!locationClient.isStarted()) {
            locationClient.start();
            //开启方向传感器
            myOritationListener.start();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        //停止定位
        baiduMap.setMyLocationEnabled(false);
        locationClient.stop();
        //停止方向传感器
        myOritationListener.stop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
        if(mPoiSearch != null){
            mPoiSearch.destroy();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }
}
