package com.hkc.mymapy;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
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
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.hkc.listener.MyOritationListener;
import com.hkc.utitls.ScreenUtils;

public class RouteplanActivity extends AppCompatActivity implements View.OnClickListener {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routeplan);

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
