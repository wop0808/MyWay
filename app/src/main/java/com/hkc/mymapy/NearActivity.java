package com.hkc.mymapy;

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

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.hkc.adapter.NearAdapter;

import java.util.ArrayList;

public class NearActivity extends AppCompatActivity implements View.OnClickListener, OnGetSuggestionResultListener {
    private TextView tv_back;
    private NearAdapter nearAdapter;
    private ListView lv_near;
    private ImageView iv_search;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayList<String> suggest;
    private ArrayAdapter<String> sugAdapter;
    private SuggestionSearch mSuggestionSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near);

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
    public void onClick(View v) {
        switch (v.getId()) {
            //返回
            case R.id.id_main_near_tv_back:
                this.finish();
                break;
            //搜索
            case R.id.id_main_near_search:

                break;
        }
    }

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
}

