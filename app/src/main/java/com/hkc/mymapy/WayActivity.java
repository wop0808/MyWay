package com.hkc.mymapy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.hkc.adapter.WayAdapter;

import java.util.ArrayList;

public class WayActivity extends AppCompatActivity implements View.OnClickListener, OnGetSuggestionResultListener, TextWatcher {
    private WayAdapter wayAdapter;
    private ImageView iv_back,iv_car,iv_bus,iv_walk;
    private TextView tv_search;
    private AutoCompleteTextView act_destination,act_mylocation;
    private ListView listView;
    private ImageView iv_change;
    private String TAG = "crazyK";
    //点击搜索时三种出行状态 car：1 bus：2 walk：3
    private final int MODE_CAR = 1;
    private final int MODE_BUS = 2;
    private final int MODE_WALK = 3;
    //记录出发地的标志，0为我的位置，1为其他位置
    private int flag = -1;
    //建议搜索模块
    private SuggestionSearch mSuggestionSearch;
    private ArrayList<String> suggest;
    private ArrayAdapter<String> sugAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_way);



        iv_back = (ImageView) findViewById(R.id.id_main_way_iv_back);
        iv_car = (ImageView) findViewById(R.id.id_main_way_car);
        iv_bus = (ImageView) findViewById(R.id.id_main_way_bus);
        iv_walk = (ImageView) findViewById(R.id.id_main_way_walk);
        tv_search = (TextView) findViewById(R.id.id_main_way_tv_search);
        act_destination = (AutoCompleteTextView) findViewById(R.id.id_way_act_destination);
        iv_change = (ImageView) findViewById(R.id.id_main_way_change);
        wayAdapter = new WayAdapter(this);
        act_mylocation = (AutoCompleteTextView) findViewById(R.id.id_way_act_myposition);

        act_destination.addTextChangedListener(this);
        act_mylocation.addTextChangedListener(this);


        iv_back.setOnClickListener(this);
        iv_car.setOnClickListener(this);
        iv_bus.setOnClickListener(this);
        iv_walk.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        iv_change.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.id_main_way_lv);
        listView.setEmptyView(findViewById(R.id.id_main_way_emptyview));
        listView.setAdapter(wayAdapter);

        iv_car.setSelected(true);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //搜索
            case R.id.id_main_way_tv_search:
//                Toast.makeText(this,"tv_search",Toast.LENGTH_SHORT).show();

                //出行方式的选择
                int MODE_search = 0;
                //获取我的位置：stNode, 终点位置：enNode
                String myLocStr = act_mylocation.getText().toString().trim();
                String enNodeStr = act_destination.getText().toString().trim();
                String stNodeStr = null;
                LatLng myLoc_LatLng = null;

                //终点为空
                if( TextUtils.isEmpty(enNodeStr) ){
                    Toast.makeText(WayActivity.this, "请输入有效地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                //起点为空，或者为我的位置
                if( TextUtils.isEmpty(myLocStr) || TextUtils.equals(myLocStr,"我的位置")){
                    flag = 0 ;
//                    Log.i(TAG, "myLoc_LatLng:" + myLoc_LatLng);
                }
                //起点不为空，且为有效地址
                if(!TextUtils.isEmpty(myLocStr) && !TextUtils.equals(myLocStr,"我的位置")){
                    stNodeStr = myLocStr;
                    flag = 1;
//                    Log.i(TAG, "stNode: " + stNode);
                }

                //判断出行方式
                if(iv_car.isSelected()){
                    MODE_search = MODE_CAR;
                }else if(iv_bus.isSelected()){
                    MODE_search = MODE_BUS;
                }else if(iv_walk.isSelected()){
                    MODE_search =  MODE_WALK;
                }
                if(MODE_search == 0){
                    Toast.makeText(WayActivity.this, "出行方式选择错误", Toast.LENGTH_SHORT).show();
                    return;
                }


                Intent intent_WayToRouteplan = new Intent(this,RouteplanActivity.class);
                intent_WayToRouteplan.putExtra("stNodeStr",stNodeStr);
                intent_WayToRouteplan.putExtra("flag",flag);
                intent_WayToRouteplan.putExtra("enNodeStr",enNodeStr);
                intent_WayToRouteplan.putExtra("MODE_search",MODE_search);

                startActivity(intent_WayToRouteplan);

                break;
            //返回
            case R.id.id_main_way_iv_back:
                this.finish();
                break;
            //驾车
            case R.id.id_main_way_car:
                iv_car.setSelected(true);
                iv_bus.setSelected(false);
                iv_walk.setSelected(false);
                break;
            //公交
            case R.id.id_main_way_bus:
                iv_car.setSelected(false);
                iv_bus.setSelected(true);
                iv_walk.setSelected(false);
                break;
            //步行
            case R.id.id_main_way_walk:
                iv_car.setSelected(false);
                iv_bus.setSelected(false);
                iv_walk.setSelected(true);
                break;
            //始发地改变
            case R.id.id_main_way_change:
                Toast.makeText(this,"iv_change",Toast.LENGTH_SHORT).show();
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
        act_mylocation.setAdapter(sugAdapter);
        act_destination.setAdapter(sugAdapter);
        sugAdapter.notifyDataSetChanged();
    }

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

    @Override
    protected void onStart() {
        super.onStart();
        if(mSuggestionSearch == null){
            // 初始化建议搜索模块，注册建议搜索事件监听
            mSuggestionSearch = SuggestionSearch.newInstance();
            mSuggestionSearch.setOnGetSuggestionResultListener(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mSuggestionSearch!=null){
            mSuggestionSearch.destroy();
        }
    }
}
