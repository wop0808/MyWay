package com.hkc.mymapy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.Poi;
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
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.hkc.adapter.Search_Adapter;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, OnGetSuggestionResultListener, OnGetPoiSearchResultListener {
    private final int resultCode_SearchToMain = 1;
    //可视化控件
    private LinearLayout search ;
    private LinearLayout back;
    private AutoCompleteTextView act_search;
    //地理编码检索
    private GeoCoder geoCoder;
    //建议搜索模块
    private SuggestionSearch mSuggestionSearch;
    private ArrayList<String> suggest;
    private ArrayAdapter<String> sugAdapter;
    private PoiSearch mPoiSearch;

//    //从Main传来的当前城市
//    String currentCity;

    private ListView lv;
    private Search_Adapter search_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

//        //从Main传来的当前城市
//        Intent intent_SearchFromMain = getIntent();
//        currentCity = intent_SearchFromMain.getStringExtra("currentCity");

        lv = (ListView) findViewById(R.id.id_search_lv);
        search_adapter = new Search_Adapter();
        lv.setAdapter(search_adapter);

        init();

        search.setOnClickListener(this);
        back.setOnClickListener(this);

        //自动补全搜索栏绑定输入监听
        act_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    return;
                }
                //使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                mSuggestionSearch
                        .requestSuggestion((new SuggestionSearchOption())
                                .keyword(s.toString()).city(MainActivity.currentCity));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void init(){
        search = (LinearLayout) findViewById(R.id.id_search_fangdajing);
        back = (LinearLayout) findViewById(R.id.id_search_back);
        act_search = (AutoCompleteTextView) findViewById(R.id.id_search_search_colum);
//        geoCoder = GeoCoder.newInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //搜索按键
            case R.id.id_search_fangdajing:
                Toast.makeText(SearchActivity.this, "已点击", Toast.LENGTH_SHORT).show();
                String search_content = act_search.getText().toString().trim();
                if(search_content != null){
                    mPoiSearch.searchInCity((new PoiCitySearchOption())
                            .city(MainActivity.currentCity).keyword(search_content).pageNum(0));
                }else {
                    Toast.makeText(this, "地址不能为空", Toast.LENGTH_SHORT).show();
                }
//                if(TextUtils.isEmpty(search_content)){
//                    Toast.makeText(SearchActivity.this, "请输入有效地址", Toast.LENGTH_SHORT).show();
//                }else {
//                    geoCoder = GeoCoder.newInstance();
//                    geoCoder.setOnGetGeoCodeResultListener(this);
//
//                    //正向解析（地址-->坐标）
//                    //Log.e("-----", MainActivity.currentAddress.city);
////                    geoCoder.geocode(new GeoCodeOption().city(MainActivity.currentAddress.city).address(search_content));
//                    geoCoder.geocode(new GeoCodeOption().city(currentCity).address(search_content));
//                }
                break;
            //返回
            case R.id.id_search_back:
                this.finish();
                break;

        }
    }


//    @Override
//    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
//        if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
//            Toast.makeText(this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
//                    .show();
//            return;
//        }
//        /*mBaiduMap.clear();
//        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
//                .icon(BitmapDescriptorFactory
//                        .fromResource(R.drawable.icon_marka)));
//        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
//                .getLocation()));
//        String strInfo = String.format("纬度：%f 经度：%f",
//                result.getLocation().latitude, result.getLocation().longitude);
//        Toast.makeText(GeoCoderDemo.this, strInfo, Toast.LENGTH_LONG).show();*/
//
//        LatLng latLng = new LatLng(geoCodeResult.getLocation().latitude,geoCodeResult.getLocation().longitude);
//        Intent intent = this.getIntent();
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("position",latLng);
//        intent.putExtras(bundle);
//        this.setResult(resultCode_SearchToMain,intent);
//        this.finish();
//    }
//
//    @Override
//    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
//
//
//    }


    //自动补全搜索栏返回的结果
    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
            return;
        }
        suggest = new ArrayList<String>();
        for (SuggestionResult.SuggestionInfo info : suggestionResult.getAllSuggestions()) {
            if (info.key != null) {
                suggest.add(info.key);
            }
        }
        sugAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, suggest);
        act_search.setAdapter(sugAdapter);
        sugAdapter.notifyDataSetChanged();
    }

    //以下三个是POI搜索返回的结果
    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(this, "未找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        } else if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
            Intent intent_SearchToMain = new Intent();
            intent_SearchToMain.putExtra("poiResult", poiResult);
            this.setResult(resultCode_SearchToMain, intent_SearchToMain);
            this.finish();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    protected void onDestroy() {
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        super.onDestroy();
    }
}
