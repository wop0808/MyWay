package com.hkc.mymapy;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.hkc.fragment.Fragment_collect_point;
import com.hkc.fragment.Fragment_collect_way;

public class CollectionActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener ,View.OnClickListener{
    private RadioGroup radioGroup;//收藏的点、收藏的路线
    private FragmentManager fragmentManager;
    private Fragment_collect_point fragment_collect_point;//收藏的点的fragment
    private Fragment_collect_way fragment_collect_way;//收藏路线fragment
    private ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        fragmentManager = this.getSupportFragmentManager();

        radioGroup = (RadioGroup) findViewById(R.id.id_collect_rg);
        iv_back = (ImageView) findViewById(R.id.id_collect_back);

        radioGroup.setOnCheckedChangeListener(this);
        iv_back.setOnClickListener(this);

        initFragment();
    }
    //初始化收藏的点的fragment
    public void initFragment(){
        if(fragment_collect_point == null){
            fragment_collect_point = new Fragment_collect_point();
        }
        fragmentManager.beginTransaction().replace(R.id.id_collect_fragment,fragment_collect_point).commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            //收藏的点
            case R.id.id_collection_point:
                if(fragment_collect_point == null){
                    fragment_collect_point = new Fragment_collect_point();
                }
                fragmentManager.beginTransaction().replace(R.id.id_collect_fragment,fragment_collect_point).commit();
                break;
            //收藏的路线
            case R.id.id_collect_way:
                if(fragment_collect_way == null){
                    fragment_collect_way = new Fragment_collect_way();
                }
                fragmentManager.beginTransaction().replace(R.id.id_collect_fragment,fragment_collect_way).commit();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回键
            case R.id.id_collect_back:
                this.finish();
                break;
        }
    }
}
