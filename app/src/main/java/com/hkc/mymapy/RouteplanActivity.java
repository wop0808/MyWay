package com.hkc.mymapy;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;

public class RouteplanActivity extends AppCompatActivity {
    private SupportMapFragment supportMapFragment;
//    private Frag_Routeplan_Car frag_routeplan_car;
//    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routeplan);

//        Intent intent = getIntent();
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(MainActivity.currentLatLng);
//        if (intent.hasExtra("x") && intent.hasExtra("y")) {
//            // 当用intent参数时，设置中心点为指定点
//            Bundle b = intent.getExtras();
//            LatLng p = new LatLng(b.getDouble("y"), b.getDouble("x"));
//            builder.target(p);
//        }
        builder.overlook(-20).zoom(19.5f);
        BaiduMapOptions bo = new BaiduMapOptions().mapStatus(builder.build())
                .compassEnabled(false).zoomControlsEnabled(false);
        supportMapFragment = SupportMapFragment.newInstance(bo);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.id_routeplan_fragment, supportMapFragment, "map_fragment").commit();

//        fragmentManager = getSupportFragmentManager();
//
//        if (frag_routeplan_car == null){
//            frag_routeplan_car = new Frag_Routeplan_Car();
//            fragmentManager.beginTransaction().replace(R.id.id_routeplan_fragment,frag_routeplan_car).commit();
//        }else {
//            fragmentManager.beginTransaction().replace(R.id.id_routeplan_fragment,frag_routeplan_car).commit();
//        }
    }



}
