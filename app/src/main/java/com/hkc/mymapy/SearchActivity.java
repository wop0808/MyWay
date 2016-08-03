package com.hkc.mymapy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.hkc.adapter.Search_Adapter;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener,OnGetGeoCoderResultListener{
    private final int resultCode_SearchToMain = 1;
    //可视化控件
    private LinearLayout search ;
    private LinearLayout back;
    private EditText et_search;
    //地理编码检索
    private GeoCoder geoCoder;

    //从Main传来的当前城市
    String currentCity;

    private ListView lv;
    private Search_Adapter search_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //从Main传来的当前城市
        Intent intent_SearchFromMain = getIntent();
        currentCity = intent_SearchFromMain.getStringExtra("currentCity");

        lv = (ListView) findViewById(R.id.id_search_lv);
        search_adapter = new Search_Adapter();
        lv.setAdapter(search_adapter);

        init();

        search.setOnClickListener(this);
        back.setOnClickListener(this);
        et_search.setOnClickListener(this);


    }

    public void init(){
        search = (LinearLayout) findViewById(R.id.id_search_fangdajing);
        back = (LinearLayout) findViewById(R.id.id_search_back);
        et_search = (EditText) findViewById(R.id.id_search_search_colum);
//        geoCoder = GeoCoder.newInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //搜索按键
            case R.id.id_search_fangdajing:
                Toast.makeText(SearchActivity.this, "已点击", Toast.LENGTH_SHORT).show();
                String search_content = et_search.getText().toString().trim();
                if(TextUtils.isEmpty(search_content)){
                    Toast.makeText(SearchActivity.this, "请输入有效地址", Toast.LENGTH_SHORT).show();
                }else {
                    geoCoder = GeoCoder.newInstance();
                    geoCoder.setOnGetGeoCodeResultListener(this);

                    //正向解析（地址-->坐标）
                    //Log.e("-----", MainActivity.currentAddress.city);
//                    geoCoder.geocode(new GeoCodeOption().city(MainActivity.currentAddress.city).address(search_content));
                    geoCoder.geocode(new GeoCodeOption().city(currentCity).address(search_content));
                }
                break;
            //返回
            case R.id.id_search_back:
                this.finish();
                break;

        }
    }


    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        /*mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_marka)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));
        String strInfo = String.format("纬度：%f 经度：%f",
                result.getLocation().latitude, result.getLocation().longitude);
        Toast.makeText(GeoCoderDemo.this, strInfo, Toast.LENGTH_LONG).show();*/

        LatLng latLng = new LatLng(geoCodeResult.getLocation().latitude,geoCodeResult.getLocation().longitude);
        Intent intent = this.getIntent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("position",latLng);
        intent.putExtras(bundle);
        this.setResult(resultCode_SearchToMain,intent);
        this.finish();
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

    }


}
