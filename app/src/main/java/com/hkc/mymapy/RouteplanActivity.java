package com.hkc.mymapy;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
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
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.hkc.listener.MyOritationListener;
import com.hkc.utitls.ScreenUtils;

public class RouteplanActivity extends AppCompatActivity implements View.OnClickListener, OnGetRoutePlanResultListener {
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
    private ImageView iv_car,iv_bus,iv_walk;
    //路线计划搜索相关
    private RoutePlanSearch routePlanSearch;
    //搜索结果中间变脸
    private DrivingRouteResult nowResultd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routeplan);

        Intent intent_RouteFromWay = getIntent();
        Mode_Search = intent_RouteFromWay.getIntExtra("MODE_search", -1);
        flag = intent_RouteFromWay.getIntExtra("flag",-1);
        stNodeStr = intent_RouteFromWay.getStringExtra("stNode");
        enNodeStr = intent_RouteFromWay.getStringExtra("enNode");

        // 初始化搜索模块，注册事件监听
        routePlanSearch = RoutePlanSearch.newInstance();
        routePlanSearch.setOnGetRoutePlanResultListener(this);

        initView();
        initListener();
        initMapRelate();



    }

    //判断出行方式，并且设置上方导航栏的select属性
    //在定位相关中 获取currentLatlng后调用
    //判断出行方式MODE_search，出发地址flag、stNode，终止地址enNode
    //三种出行状态MODE_search: car：1 bus：2 walk：3 ,出发地的标志flag，0为我的位置，1为其他位置
    //flag = 0 时，stNode为null；flag = 1时 ，stNode为地址名称
    /**
     * 代码逻辑仍然有问题，不能支持自动全国搜索
     * */
    public void whichWay(int Mode_Search,int flag,String stNodeStr ,String enNodeStr){
       //获得终点enNode
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("成都",enNodeStr);
        Log.i(TAG, "调用whichWay: ");
        switch (Mode_Search){
            //car出行
            case 1:
                iv_car.setSelected(true);
                iv_bus.setSelected(false);
                iv_walk.setSelected(false);
                if(flag == 0 ){

                    PlanNode stNode = PlanNode.withLocation(currentLatLng);
                    routePlanSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
                    Log.i(TAG, "car出行,我的位置");
                }else if(flag == 1){
                    Log.i(TAG, "car出行,不是我的位置");
                    if(stNodeStr == null){
                        Toast.makeText(RouteplanActivity.this, "开始地点stNode为空", Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        PlanNode stNode = PlanNode.withCityNameAndPlaceName("成都",enNodeStr);
                        routePlanSearch.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(enNode));
                    }

                }

                break;
            //bus出行
            case 2:
                iv_car.setSelected(false);
                iv_bus.setSelected(true);
                iv_walk.setSelected(false);
                if(flag == 0 ){
                    Log.i(TAG, "bus出行");
                }
                break;
            //walk出行
            case 3:
                iv_car.setSelected(false);
                iv_bus.setSelected(true);
                iv_walk.setSelected(false);
                if(flag == 0 ){
                    Log.i(TAG, "walk出行");
                }
                break;
            //出行方式传值错误
            case -1:
                Log.i(TAG, "Mode_Search = -1 ");
                break;
        }
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
            Toast.makeText(RouteplanActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            Log.i(TAG, "起终点或途经点地址有岐义，通过以下接口获取建议查询信息: ");
            return;
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
//            nodeIndex = -1;
            Log.i(TAG, "开始驾车路线查询");


            if (drivingRouteResult.getRouteLines().size() > 1 ) {
                nowResultd = drivingRouteResult;

                String title = drivingRouteResult.getRouteLines().get(0).getTitle();
                String title1 = drivingRouteResult.getRouteLines().get(0).getStarting().getTitle();
                Log.i(TAG, "title =  "+title +"\n" +"起点title"+ title1);

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

            } /*else if ( result.getRouteLines().size() == 1 ) {
                route = result.getRouteLines().get(0);
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
                routeOverlay = overlay;
                mBaidumap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
                mBtnPre.setVisibility(View.VISIBLE);
                mBtnNext.setVisibility(View.VISIBLE);
            }*/

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
    }

    //回到中心位置
    public void centerToMyLocation(LatLng latLng) {
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(mapStatusUpdate);
    }

    //初始化 地图定位相关 ，方向传感器
    public void initMapRelate(){
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
        whichWay(this.Mode_Search, this.flag,this.stNodeStr,this.enNodeStr);



    }




    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            MyLocationData myLocationData = new MyLocationData.Builder()
                    .direction(currentX)
                    .accuracy(bdLocation.getRadius())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();

            baiduMap.setMyLocationData(myLocationData);
            //设置自定义图标
            MyLocationConfiguration configuration = new MyLocationConfiguration(locationMode, true, iconLocation);
            baiduMap.setMyLocationConfigeration(configuration);

            currentLatLng = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
                Log.i(TAG, "currentLatLng: "+currentLatLng);
                break;

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
