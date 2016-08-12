package com.hkc.mymapy;


import android.content.Intent;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.hkc.adapter.Vp_AddressInfo_Adapter;
import com.hkc.fragment.Fragment_popup1;
import com.hkc.listener.MyOritationListener;
import com.hkc.utitls.PoiOverlay;
import com.hkc.utitls.ScreenUtils;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private final String TAG = "crazyK";

    private final int RequestCode_mainToSearch = 1;
    private final int RequestCode_mainToNear = 2;
    //路况按钮
    private TextView tv_lukuang, tv_search_colum;
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
    //导航栏
    static public ImageView tv_near, tv_way, tv_my;
    //fragmentManager
    static public FragmentManager fragmentManager;
    //定位
    private LocationClient locationClient;
    private MyLocationListener myLocationListener;
    private boolean isFirstIn = true;
    //当前经纬度
    public static double curentLatitude;//纬度
    public static double curentLongtitude;//经度
    public static LatLng currentLatLng;//当前坐标
    //当前城市
    public static String currentCity;
    //自定义定位图标
    private BitmapDescriptor iconLocation;
    //自定义方向传感器监听
    private MyOritationListener myOritationListener;
    //当前位置
    private float currentX;
    //模式切换
    private MyLocationConfiguration.LocationMode locationMode;
    int count_search = 0;
    //POI查询相关
    private PoiSearch mPoiSearch;
    private int radius_Nearby = 500;
    //视图中隐藏的viewpager 用于选点显示地点信息
    private RelativeLayout rl_vp;
    private ImageView iv_routePlan;
    private ViewPager vp_AddressInfo;
    private Vp_AddressInfo_Adapter vp_addressInfo_adapter;
    //点击搜索后得到的poiResult
    private PoiResult poiResult;
    //点击搜索结果marker 以及 滑动viewpager时获得的poiInfo
    private PoiInfo poiInfo_FromMarkerAndVp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);


        //初始化view //初始化changeMap ,PopupWindow
        initView();
        //初始化地图标尺
        initMetre();
        //设置监听
        initListener();
        //开始定位
        startLocation();


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
        if (mPoiSearch != null) {
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


    public void initView() {
        //获取地图控件引用
        mapView = (MapView) findViewById(R.id.id_main_mapview);
        baiduMap = mapView.getMap();
        //地图内控件
        tv_near = (ImageView) findViewById(R.id.id_main_tv_near);
        tv_my = (ImageView) findViewById(R.id.id_main_tv_my);
        tv_way = (ImageView) findViewById(R.id.id_main_tv_way);
        tv_lukuang = (TextView) findViewById(R.id.id_main_lukuang);
        tv_search_colum = (TextView) findViewById(R.id.id_main_search_colum);
        iv_find = (ImageView) findViewById(R.id.id_main_main_find);
        iv_changemap = (ImageView) findViewById(R.id.id_main_changemap);
        rl_vp = (RelativeLayout) findViewById(R.id.id_main_rl_vp);
        vp_AddressInfo = (ViewPager) findViewById(R.id.id_main_viewpager_addressinfo);
        iv_routePlan = (ImageView) findViewById(R.id.id_main_popupwindow_marker_iv_daohang);
        //获取fragment Manager
        fragmentManager = this.getSupportFragmentManager();
        //设置地图为普通地图
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        initPopWindow();
        tv_changemap_biaozhun.setSelected(true);
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
        tv_search_colum.setOnClickListener(this);
        iv_find.setOnClickListener(this);
        tv_near.setOnClickListener(this);
        tv_way.setOnClickListener(this);
        tv_my.setOnClickListener(this);
        vp_AddressInfo.setOnPageChangeListener(this);
        iv_routePlan.setOnClickListener(this);
    }

    //初始化地图标尺长度，大概20m
    public void initMetre() {
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(19.5f);
        baiduMap.setMapStatus(mapStatusUpdate);
    }

    //开启定位
    public void startLocation() {
        locationMode = MyLocationConfiguration.LocationMode.NORMAL;
        locationClient = new LocationClient(this);
        myLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(myLocationListener);

        LocationClientOption locationClientOption = new LocationClientOption();
        locationClientOption.setCoorType("bd09ll");//设置坐标类型
        locationClientOption.setIsNeedAddress(true);
        locationClientOption.setOpenGps(true);
        locationClientOption.setScanSpan(1000);


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

    //回到中心位置
    public void centerToMyLocation(LatLng latLng) {

//        LatLng latLng = new LatLng(curentLatitude, curentLongtitude);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(mapStatusUpdate);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //附近页面
            case R.id.id_main_tv_near:
                Intent intent_MainToNear = new Intent(this, NearActivity.class);
                startActivityForResult(intent_MainToNear, RequestCode_mainToNear);
                break;
            //我的页面
            case R.id.id_main_tv_my:
                Intent intent_MainToMy = new Intent(this, MyActivity.class);
                startActivity(intent_MainToMy);
                break;
            //路线页面
            case R.id.id_main_tv_way:
                Intent intent_MainToWay = new Intent(this, WayActivity.class);
                startActivity(intent_MainToWay);
                break;
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
            case R.id.id_main_changemap:
                if (!popupWindow_changemap.isShowing()) {
                    int position[] = new int[2];
                    v.getLocationInWindow(position);
                    popupWindow_changemap.showAtLocation(v, Gravity.RIGHT | Gravity.TOP, ScreenUtils.screenWidth(this) - position[0] - v.getWidth(), position[1] + v.getHeight());
                } else {
                    popupWindow_changemap.dismiss();
                }
                break;
            //路况按钮
            case R.id.id_main_lukuang:
                if (count_lukuang++ % 2 == 0) {
                    tv_lukuang.setSelected(true);
                    baiduMap.setTrafficEnabled(true);
                } else {
                    tv_lukuang.setSelected(false);
                    baiduMap.setTrafficEnabled(false);
                }
                break;
            //搜索栏
            case R.id.id_main_search_colum:
                Intent intent_MainToSearch = new Intent(this, SearchActivity.class);
//                intent_MainToSearch.putExtra("currentCity", currentCity);
                startActivityForResult(intent_MainToSearch, RequestCode_mainToSearch);
                break;
            //发现(回归原点 并切换模式)
            case R.id.id_main_main_find:
                if (++count_search % 2 == 1) {
                    locationMode = MyLocationConfiguration.LocationMode.COMPASS;
                    Toast.makeText(MainActivity.this, "罗盘", Toast.LENGTH_SHORT).show();
                } else {
                    locationMode = MyLocationConfiguration.LocationMode.NORMAL;
                    Toast.makeText(MainActivity.this, "普通", Toast.LENGTH_SHORT).show();
                }
                centerToMyLocation(currentLatLng);
                break;
        }
    }

    //地址信息viewpager滑动监听
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        poiInfo_FromMarkerAndVp = poiResult.getAllPoi().get(position);
        centerToMyLocation(poiInfo_FromMarkerAndVp.location);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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

            //获取经纬度
            curentLatitude = bdLocation.getLatitude();
            curentLongtitude = bdLocation.getLongitude();
            currentLatLng = new LatLng(curentLatitude, curentLongtitude);
            //获取当前城市
            currentCity = bdLocation.getCity();


            //首次定位
            if (isFirstIn) {
                LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                baiduMap.animateMapStatus(mapStatusUpdate);
                isFirstIn = false;
            }
        }
    }

    //code=1 表示从SearchActivity中返回的搜索坐标
    //code=2 表示从NearActivity中返回的POI搜索结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(rl_vp.getVisibility() == View.VISIBLE){
            rl_vp.setVisibility(View.GONE);
        }
        if (requestCode == 1 && resultCode == 1) {
            poiResult = data.getParcelableExtra("poiResult");
//            Log.i(TAG, poiResult.toString());
            baiduMap.clear();
            PoiOverlay overlay_FromSearch = new MyPoiOverlay(baiduMap);
            baiduMap.setOnMarkerClickListener(overlay_FromSearch);
            overlay_FromSearch.setData(poiResult);
            overlay_FromSearch.addToMap();
            overlay_FromSearch.zoomToSpan();

            //蛟神黑科技 将activityi获得的数据-->adapter -->fragment
            //避免了控件在onCreateView方法还未执行完时，对控件进行操作，爆出空指针异常
            if (vp_AddressInfo.getAdapter() == null) {
                vp_addressInfo_adapter = new Vp_AddressInfo_Adapter(fragmentManager);
                vp_AddressInfo.setAdapter(vp_addressInfo_adapter);
            }
            vp_addressInfo_adapter.setPoiResult(poiResult);
//            Toast.makeText(this,"新搜索",Toast.LENGTH_SHORT).show();


            /**
             * 逻辑还需完善 使其能自动搜索
             * */
            if (poiResult.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

                // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
                String strInfo = "在";
                for (CityInfo cityInfo : poiResult.getSuggestCityList()) {
                    strInfo += cityInfo.city;
                    strInfo += ",";
                }
                strInfo += "找到结果";
                Toast.makeText(this, strInfo, Toast.LENGTH_LONG)
                        .show();
            }
        } else if (requestCode == 2 && resultCode == 2) {
//            PoiResult poiResult = (PoiResult) data.getSerializableExtra("poiResult");

            poiResult = data.getParcelableExtra("poiResult");

//            Log.i(TAG, poiResult.toString());
            baiduMap.clear();
            PoiOverlay overlay_FromNear = new MyPoiOverlay(baiduMap);
            baiduMap.setOnMarkerClickListener(overlay_FromNear);
            overlay_FromNear.setData(poiResult);
            overlay_FromNear.addToMap();
            overlay_FromNear.zoomToSpan();

            //蛟神黑科技 将activityi获得的数据-->adapter -->fragment
            //避免了控件在onCreateView方法还未执行完时，对控件进行操作，爆出空指针异常
            if (vp_AddressInfo.getAdapter() == null) {
                vp_addressInfo_adapter = new Vp_AddressInfo_Adapter(fragmentManager);
                vp_AddressInfo.setAdapter(vp_addressInfo_adapter);
            }
            vp_addressInfo_adapter.setPoiResult(poiResult);

            /**
             * 逻辑还需完善 使其能自动搜索
             * */
            if (poiResult.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

                // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
                String strInfo = "在";
                for (CityInfo cityInfo : poiResult.getSuggestCityList()) {
                    strInfo += cityInfo.city;
                    strInfo += ",";
                }
                strInfo += "找到结果";
                Toast.makeText(this, strInfo, Toast.LENGTH_LONG)
                        .show();
            }
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
            mPoiSearch = PoiSearch.newInstance();
            centerToMyLocation(poiInfo_FromMarkerAndVp.location);

            rl_vp.setVisibility(View.VISIBLE);
            mapView.showZoomControls(false);

            vp_AddressInfo.setCurrentItem(index, true);

            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(poiInfo_FromMarkerAndVp.uid));
            // }
            return true;
        }
    }

}


