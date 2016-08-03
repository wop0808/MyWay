package com.hkc.mymapy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.hkc.adapter.NearAdapter;
import com.hkc.utitls.PoiOverlay;

import java.util.ArrayList;

public class NearActivity extends AppCompatActivity implements View.OnClickListener, OnGetSuggestionResultListener ,OnGetPoiSearchResultListener{
    private TextView tv_back;
    private NearAdapter nearAdapter;
    private ListView lv_near;
    private ImageView iv_search;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayList<String> suggest;
    private ArrayAdapter<String> sugAdapter;
    private SuggestionSearch mSuggestionSearch;
    private PoiSearch mPoiSearch;
    private final int RESULTCODE_NEARTOMAIN = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near);

        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        tv_back = (TextView) findViewById(R.id.id_main_near_tv_back);
        iv_search = (ImageView) findViewById(R.id.id_main_near_search);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.id_near_autoCompleteTextView);

        //搜索栏设置自动补全监听
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
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


        tv_back.setOnClickListener(this);
        iv_search.setOnClickListener(this);

        //设置图片listview
        lv_near = (ListView) findViewById(R.id.id_nearlayout_lv);
        if (nearAdapter == null) {
            nearAdapter = new NearAdapter(this);
        }
        lv_near.setAdapter(nearAdapter);

    }

    @Override
    protected void onDestroy() {
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.id_main_near_tv_back:
                this.finish();
                break;
            //搜索
            case R.id.id_main_near_search:
                String keystr = autoCompleteTextView.getText().toString();
                if(keystr != null){
                    mPoiSearch.searchInCity((new PoiCitySearchOption())
                            .city(MainActivity.currentCity).keyword(keystr).pageNum(0));
                }else {
                    Toast.makeText(NearActivity.this, "地址不能为空", Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }

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
        autoCompleteTextView.setAdapter(sugAdapter);
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
            Intent intent_NearToMain = new Intent();
            intent_NearToMain.putExtra("poiResult", poiResult);
            this.setResult(RESULTCODE_NEARTOMAIN, intent_NearToMain);
            this.finish();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }


}

