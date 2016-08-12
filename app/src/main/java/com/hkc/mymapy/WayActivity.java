package com.hkc.mymapy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hkc.adapter.WayAdapter;

public class WayActivity extends AppCompatActivity implements View.OnClickListener{
    private WayAdapter wayAdapter;
    private ImageView iv_back,iv_car,iv_bus,iv_walk;
    private TextView tv_search;
    private EditText et_destination,et_mylocation;
    private ListView listView;
    private ImageView iv_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_way);

        iv_back = (ImageView) findViewById(R.id.id_main_way_iv_back);
        iv_car = (ImageView) findViewById(R.id.id_main_way_car);
        iv_bus = (ImageView) findViewById(R.id.id_main_way_bus);
        iv_walk = (ImageView) findViewById(R.id.id_main_way_walk);
        tv_search = (TextView) findViewById(R.id.id_main_way_tv_search);
        et_destination = (EditText) findViewById(R.id.id_main_way_destination);
        iv_change = (ImageView) findViewById(R.id.id_main_way_change);
        wayAdapter = new WayAdapter(this);
        et_mylocation = (EditText) findViewById(R.id.id_main_way_et_myposition);


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
                Toast.makeText(this,"tv_search",Toast.LENGTH_SHORT).show();

                //获取我的位置：stNode, 终点位置：enNode
                String stNode = et_mylocation.getText().toString().trim();
                String enNode = et_destination.getText().toString().trim();

                Intent intent_WayToRouteplan = new Intent(this,RouteplanActivity.class);
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
