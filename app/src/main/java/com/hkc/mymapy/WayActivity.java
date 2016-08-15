package com.hkc.mymapy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.hkc.adapter.WayAdapter;

public class WayActivity extends AppCompatActivity implements View.OnClickListener{
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
                String enNode = act_destination.getText().toString().trim();
                String stNode = null;
                LatLng myLoc_LatLng = null;

                if( TextUtils.isEmpty(enNode) ){
                    Toast.makeText(WayActivity.this, "请输入有效地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                if( TextUtils.isEmpty(myLocStr) || TextUtils.equals(myLocStr,"我的位置")){
                    myLoc_LatLng = MainActivity.currentLatLng;
//                    Log.i(TAG, "myLoc_LatLng:" + myLoc_LatLng);
                }
                if(!TextUtils.isEmpty(myLocStr) && !TextUtils.equals(myLocStr,"我的位置")){
                    stNode = myLocStr;
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
                intent_WayToRouteplan.putExtra("stNode",stNode);
                intent_WayToRouteplan.putExtra("myLoc_LatLng",myLoc_LatLng);
                intent_WayToRouteplan.putExtra("enNode",enNode);
                intent_WayToRouteplan.putExtra("MODE_search",MODE_search);

                startActivity(intent_WayToRouteplan);





//                RoutePlanSearch routePlanSearch = RoutePlanSearch.newInstance();
//
//
//                PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京", "龙泽");
//                PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京", "西单");
//
//                routePlanSearch.transitSearch((new TransitRoutePlanOption())
//                        .from(stNode)
//                        .city("北京")
//                        .to(enNode));
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

    //判断我的位置stNode 是否为“我的位置”
    public boolean isMyLocation(String stNode){
        if(stNode.equals("我的位置")){
            return true;
        }else {
            return false;
        }
    }
}
