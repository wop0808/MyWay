package com.hkc.mymapy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Scroller;
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
import com.hkc.adapter.Vp_AddressInfo_Adapter;
import com.hkc.handler.Handler_Route_CurrentLatLng;
import com.hkc.listener.MyOritationListener;
import com.hkc.listener.OnGetCurrentLalngListener;
import com.hkc.overlay.DrivingRouteOverlay;
import com.hkc.overlay.OverlayManager;
import com.hkc.overlay.PoiOverlay;
import com.hkc.utitls.ScreenUtils;
import com.hkc.view.DriverRouteLinePlan;

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

public class RouteplanActivity extends AppCompatActivity implements View.OnClickListener, OnGetRoutePlanResultListener, OnGetCurrentLalngListener, OnGetPoiSearchResultListener, ViewPager.OnPageChangeListener {
    private final String TAG = "crazyK";
    //路况按钮
    private TextView tv_lukuang;
    private ImageView iv_find;
    private View v_popupwindow;
    private int count_lukuang = 0;
    //MapView
    private MapView mapView;
    private BaiduMap baiduMap;
    //弹窗
    private PopupWindow popupWindow_changemap;
    //改变窗口btn 弹出的三个控件
    private TextView tv_changemap_biaozhun, tv_changemap_weixing, tv_changemap_gongjiao;
    //改变窗口的按钮
    private ImageView iv_changemap;
    //fragmentManager
    static public FragmentManager fragmentManager;
    //定位
    private LocationClient locationClient;
    private MyLocationListener myLocationListener;
    //当前位置
    private LatLng currentLatLng;
    private String currentCity;
    //模式切换
    private MyLocationConfiguration.LocationMode locationMode;
    //当前位置
    private float currentX;
    //自定义定位图标
    private BitmapDescriptor iconLocation;
    //自定义方向传感器监听
    private MyOritationListener myOritationListener;
    //从 way界面传来的intent相关
    private int Mode_Search = -1;
    private int flag = -1;
    private String stNodeStr;
    private String enNodeStr;

    //导航栏三个出行方式 控件
    private ImageView iv_car, iv_bus, iv_walk;
    //路线计划搜索相关
    private RoutePlanSearch routePlanSearch;
    //搜索结果中间变量
    private DrivingRouteResult nowResult;
    //路线、覆盖物相关
    private RouteLine routeLine = null;
    private OverlayManager routeOverlay = null;
    //下方动态添加控件相关
    private LinearLayout ll_routePlanContent;
    private DriverRouteLinePlan driverRouteLinePlan;
    //当搜索结果有问题时使用的poi搜索及相关
    private RelativeLayout rl_vp;
    private ViewPager vp_addressInfo;
    private Vp_AddressInfo_Adapter vp_addressInfo_adapter;
    private PoiSearch mPoiSearch;
    private PoiResult poiResult;
    private ImageView iv_daohang;
    //点击搜索结果marker 以及 滑动viewpager时获得的poiInfo
    private PoiInfo poiInfo_FromMarkerAndVp;
    //poi搜索后获得的终点坐标
    private LatLng endLatLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routeplan);

        Intent intent_RouteFromWay = getIntent();
        Mode_Search = intent_RouteFromWay.getIntExtra("MODE_search", -1);
        flag = intent_RouteFromWay.getIntExtra("flag", -1);
        stNodeStr = intent_RouteFromWay.getStringExtra("stNodeStr");
        enNodeStr = intent_RouteFromWay.getStringExtra("enNodeStr");

        // 初始化搜索模块，注册事件监听
        routePlanSearch = RoutePlanSearch.newInstance();
        routePlanSearch.setOnGetRoutePlanResultListener(this);

        initView();
        initListener();
        initMapRelate();


    }


    //四种出行返回结果 onGetTransitRouteResult为公交
    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
        Log.i(TAG, "onGetWalkingRouteResult: ");
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
        Log.i(TAG, "onGetTransitRouteResult: ");
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        Log.i(TAG, "onGetDrivingRouteResult: ");
        if (drivingRouteResult == null || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(RouteplanActivity.this, "位置坐标不明确，请重新选择位置", Toast.LENGTH_LONG).show();
            if(mPoiSearch == null){
                // 初始化搜索模块，注册搜索事件监听
                mPoiSearch = PoiSearch.newInstance();
                mPoiSearch.setOnGetPoiSearchResultListener(this);
                mPoiSearch.searchInCity(new PoiCitySearchOption().city(currentCity).keyword(enNodeStr).pageNum(0));
            }else {
                mPoiSearch.searchInCity(new PoiCitySearchOption().city(currentCity).keyword(enNodeStr).pageNum(0));
            }

        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
//            LatLng startLocation = drivingRouteResult.getSuggestAddrInfo().getSuggestStartNode().get(0).location;
//            LatLng endLocation = drivingRouteResult.getSuggestAddrInfo().getSuggestEndNode().get(0).location;
//            PlanNode stNode = PlanNode.withLocation(startLocation);
//            PlanNode enNode = PlanNode.withLocation(endLocation);
//            routePlanSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));

            Log.i(TAG, "起终点或途经点地址有岐义，通过以下接口获取建议查询信息: ");
            return;
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
//            nodeIndex = -1;
//            Log.i(TAG, "开始驾车路线查询" + drivingRouteResult.getRouteLines().size());


            if (drivingRouteResult.getRouteLines().size() > 1) {
                this.nowResult = drivingRouteResult;
                Log.i(TAG, "路线个数："+drivingRouteResult.getRouteLines().size());

                ll_routePlanContent.removeAllViews();
                ll_routePlanContent.setWeightSum(drivingRouteResult.getRouteLines().size());

                DrivingRouteOverlay overlay = new DrivingRouteOverlay(baiduMap);
                routeOverlay = overlay;
                baiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(drivingRouteResult.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();

                for (int i = 0; i < drivingRouteResult.getRouteLines().size(); i++) {
                    routeLine = drivingRouteResult.getRouteLines().get(i);



                    //动态添加路线推荐layout
                    driverRouteLinePlan = new DriverRouteLinePlan(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
                    driverRouteLinePlan.setLayoutParams(layoutParams);
                    driverRouteLinePlan.setBackgroundColor(Color.WHITE);
                    driverRouteLinePlan.setId(i);
                    driverRouteLinePlan.setOnClickListener(this);
                    driverRouteLinePlan.setDistance(routeLine.getDistance());
                    driverRouteLinePlan.setType("方案"+(i+1));
                    driverRouteLinePlan.setTime(routeLine.getDuration());
                    ll_routePlanContent.addView(driverRouteLinePlan);
                }
//                MyTransitDlg myTransitDlg = new MyTransitDlg(RoutePlanDemo.this,
//                        result.getRouteLines(),
//                        RouteLineAdapter.Type.DRIVING_ROUTE);
//                myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
//                    public void onItemClick(int position) {
//                        route = nowResultd.getRouteLines().get(position);
//                        DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
//                        mBaidumap.setOnMarkerClickListener(overlay);
//                        routeOverlay = overlay;
//                        overlay.setData(nowResultd.getRouteLines().get(position));
//                        overlay.addToMap();
//                        overlay.zoomToSpan();
//                    }
//
//                });
//                myTransitDlg.show();

            } else if ( drivingRouteResult.getRouteLines().size() == 1 ) {
                routeLine = drivingRouteResult.getRouteLines().get(0);

                DrivingRouteOverlay overlay = new DrivingRouteOverlay(baiduMap);
                routeOverlay = overlay;
                baiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(drivingRouteResult.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }

        }
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
        Log.i(TAG, "onGetBikingRouteResult: ");
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

        ll_routePlanContent = (LinearLayout) findViewById(R.id.id_routeplan_ll_buttom_info);

        rl_vp = (RelativeLayout) findViewById(R.id.id_routeplan_rl_vp);
        vp_addressInfo = (ViewPager) findViewById(R.id.id_routeplan_viewpager_addressinfo);
        iv_daohang = (ImageView) findViewById(R.id.id_routeplan_popupwindow_marker_iv_daohang);
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
        iv_changemap.setOnClickListener(this);
        tv_changemap_biaozhun.setOnClickListener(this);
        tv_changemap_weixing.setOnClickListener(this);
        tv_changemap_gongjiao.setOnClickListener(this);
        tv_lukuang.setOnClickListener(this);
        iv_find.setOnClickListener(this);
        //导航栏三个出行方式 控件
        iv_car.setOnClickListener(this);
        iv_bus.setOnClickListener(this);
        iv_walk.setOnClickListener(this);

        iv_daohang.setOnClickListener(this);

        vp_addressInfo.setOnPageChangeListener(this);
    }

    //回到中心位置
    public void centerToMyLocation(LatLng latLng) {
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(mapStatusUpdate);
    }

    //初始化 地图定位相关 ，方向传感器
    public void initMapRelate() {
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

        //开始导航
        whichWay(this.Mode_Search);

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
    public void startSearch(LatLng currentLatLng) {
        this.currentLatLng = currentLatLng;
        //获得终点enNode
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("成都",this.enNodeStr);
        //car出行
        if(iv_car.isSelected()){
            if(flag == 0 ){
                PlanNode stNode = PlanNode.withLocation(currentLatLng);
                routePlanSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
//                Log.i(TAG, "car出行,我的位置");
            }else if(flag == 1){
//                Log.i(TAG, "car出行,不是我的位置");
                if(stNodeStr == null){
                    Toast.makeText(RouteplanActivity.this, "开始地点stNode为空", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    PlanNode stNode = PlanNode.withCityNameAndPlaceName("成都",enNodeStr);
                    routePlanSearch.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(enNode));
                }
            }

        }else if(iv_bus.isSelected()){//bus出行

        }else if(iv_walk.isSelected()){//步行

        }
    }

    //poi搜索接口的三个接口
    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        this.poiResult = poiResult;
        baiduMap.clear();
        PoiOverlay overlay_FromSearch = new MyPoiOverlay(baiduMap);
        baiduMap.setOnMarkerClickListener(overlay_FromSearch);
        overlay_FromSearch.setData(poiResult);
        overlay_FromSearch.addToMap();
        overlay_FromSearch.zoomToSpan();

        //蛟神黑科技 将activityi获得的数据-->adapter -->fragment
        //避免了控件在onCreateView方法还未执行完时，对控件进行操作，爆出空指针异常
        if (vp_addressInfo.getAdapter() == null) {
            vp_addressInfo_adapter = new Vp_AddressInfo_Adapter(fragmentManager);
            vp_addressInfo.setAdapter(vp_addressInfo_adapter);
        }
        vp_addressInfo_adapter.setPoiResult(poiResult);
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

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

            LatLng currentLatLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());

            //将currentLatLng传给主线程
            Message message = handler_Route_CurrentLatLng.obtainMessage(1);
            message.obj = currentLatLng;
            handler_Route_CurrentLatLng.sendMessage(message);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //标准地图
            case R.id.id_main_tv_changemap_biaozhun:
                tv_changemap_biaozhun.setSelected(true);
                tv_changemap_weixing.setSelected(false);
                tv_changemap_gongjiao.setSelected(false);
                baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                popupWindow_changemap.dismiss();
                break;
            //卫星地图
            case R.id.id_main_tv_changemap_weixing:
                tv_changemap_biaozhun.setSelected(false);
                tv_changemap_weixing.setSelected(true);
                tv_changemap_gongjiao.setSelected(false);
                baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                popupWindow_changemap.dismiss();
                break;
            //公交地图
            case R.id.id_main_tv_changemap_gongjiao:
                tv_changemap_biaozhun.setSelected(false);
                tv_changemap_weixing.setSelected(false);
                tv_changemap_gongjiao.setSelected(true);
                break;
            //改变地图按钮
            case R.id.id_routeplan_changemap:
                if (!popupWindow_changemap.isShowing()) {
                    int position[] = new int[2];
                    v.getLocationInWindow(position);
                    popupWindow_changemap.showAtLocation(v, Gravity.RIGHT | Gravity.TOP, ScreenUtils.screenWidth(this) - position[0] - v.getWidth(), position[1] + v.getHeight());
                } else {
                    popupWindow_changemap.dismiss();
                }
                break;
            //路况按钮
            case R.id.id_routeplan_lukuang:
                if (count_lukuang++ % 2 == 0) {
                    tv_lukuang.setSelected(true);
                    baiduMap.setTrafficEnabled(true);
                } else {
                    tv_lukuang.setSelected(false);
                    baiduMap.setTrafficEnabled(false);
                }
                break;
            //发现(回归原点 并切换模式)
            case R.id.id_routeplan_find:
                locationMode = MyLocationConfiguration.LocationMode.NORMAL;
                centerToMyLocation(currentLatLng);
//                Log.i(TAG, "currentLatLng: "+currentLatLng);
                break;
            //路线计划1
            case 0:
                DrivingRouteOverlay overlay0 = new DrivingRouteOverlay(baiduMap);
                routeOverlay = overlay0;
                baiduMap.setOnMarkerClickListener(overlay0);
                overlay0.setData(this.nowResult.getRouteLines().get(0));
                baiduMap.clear();
                overlay0.addToMap();
                overlay0.zoomToSpan();
                Toast.makeText(RouteplanActivity.this, "路线计划1", Toast.LENGTH_SHORT).show();
                break;
            //路线计划2
            case 1:
                DrivingRouteOverlay overlay1 = new DrivingRouteOverlay(baiduMap);
                routeOverlay = overlay1;
                baiduMap.setOnMarkerClickListener(overlay1);
                overlay1.setData(this.nowResult.getRouteLines().get(1));
                baiduMap.clear();
                overlay1.addToMap();
                overlay1.zoomToSpan();
                Toast.makeText(RouteplanActivity.this, "路线计划2", Toast.LENGTH_SHORT).show();
                break;
            //路线计划3
            case 2:
                DrivingRouteOverlay overlay2 = new DrivingRouteOverlay(baiduMap);
                routeOverlay = overlay2;
                baiduMap.setOnMarkerClickListener(overlay2);
                overlay2.setData(this.nowResult.getRouteLines().get(2));
                baiduMap.clear();
                overlay2.addToMap();
                overlay2.zoomToSpan();
                Toast.makeText(RouteplanActivity.this, "路线计划3", Toast.LENGTH_SHORT).show();
                break;
            //poi搜索后导航按钮被点击
            case R.id.id_routeplan_popupwindow_marker_iv_daohang:
                PlanNode stNode_Poi = PlanNode.withLocation(currentLatLng);
                PlanNode enNode_Poi = PlanNode.withLocation(endLatLng);
                if(iv_car.isSelected()){
                    routePlanSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode_Poi).to(enNode_Poi));
                }else if(iv_daohang.isSelected()){
                    routePlanSearch.transitSearch((new TransitRoutePlanOption()).from(stNode_Poi).to(enNode_Poi));
                }else if(iv_walk.isSelected()){
                    routePlanSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode_Poi).to(enNode_Poi));
                }
                if(rl_vp.getVisibility() == View.VISIBLE){
                    rl_vp.setVisibility(View.GONE);
                }
                break;

        }
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
            poiInfo_FromMarkerAndVp = getPoiResult().getAllPoi().get(index);
            //获得终点坐标，导航用
            endLatLng = poiInfo_FromMarkerAndVp.location;

            if (mPoiSearch == null){
                mPoiSearch = PoiSearch.newInstance();
            }
            centerToMyLocation(poiInfo_FromMarkerAndVp.location);

            if(rl_vp.getVisibility() == View.GONE){
                rl_vp.setVisibility(View.VISIBLE);
            }

            mapView.showZoomControls(false);

            vp_addressInfo.setCurrentItem(index, true);

            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(poiInfo_FromMarkerAndVp.uid));
            // }
            return true;
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
