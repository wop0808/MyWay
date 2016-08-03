package com.hkc.mymapy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView iv_back,iv_touxiang,tv_collection,iv_friend;
    private TextView tv_login_regist;
    private View v_ll_share,v_ll_set,v_ll_about,v_ll_clear,v_ll_update;
    private LinearLayout ll_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        iv_back = (ImageView) findViewById(R.id.id_main_my_iv_back);
        tv_collection = (ImageView) findViewById(R.id.id_main_my_collection);
        iv_touxiang = (ImageView) findViewById(R.id.id_main_my_login_regist);
        tv_login_regist = (TextView) findViewById(R.id.id_main_my_regist_login);
        v_ll_share = findViewById(R.id.id_main_my_ll_share);
        v_ll_set = findViewById(R.id.id_main_my_ll_set);
        v_ll_about = findViewById(R.id.id_main_my_ll_about);
        v_ll_clear = findViewById(R.id.id_main_my_ll_clear);
        v_ll_update = findViewById(R.id.id_main_my_ll_update);
        iv_friend = (ImageView) findViewById(R.id.id_main_my_friend);
        ll_share = (LinearLayout) findViewById(R.id.id_main_my_ll_share);

        iv_back.setOnClickListener(this);
        tv_collection.setOnClickListener(this);
        iv_touxiang.setOnClickListener(this);
        tv_login_regist.setOnClickListener(this);
        v_ll_share.setOnClickListener(this);
        v_ll_set.setOnClickListener(this);
        v_ll_about.setOnClickListener(this);
        v_ll_clear.setOnClickListener(this);
        v_ll_update.setOnClickListener(this);
        iv_friend.setOnClickListener(this);
        ll_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回
            case R.id.id_main_my_iv_back:
                this.finish();
                break;
            //收藏
            case R.id.id_main_my_collection:
                Intent intent_MyToCollection = new Intent(this, CollectionActivity.class);
                startActivity(intent_MyToCollection);
                break;
            //头像
            case R.id.id_main_my_login_regist:
                Intent intent_MyToLogin = new Intent(this, LoginActivity.class);
                startActivity(intent_MyToLogin);
                break;
            //登录/注册
            case R.id.id_main_my_regist_login:
                Intent intent_MyToLogin1 = new Intent(this, LoginActivity.class);
                startActivity(intent_MyToLogin1);
                break;
            //好友
            case R.id.id_main_my_friend:
                Toast.makeText(this,"iv_friend",Toast.LENGTH_SHORT).show();
                break;
            //分享
            case R.id.id_main_my_ll_share:
                Intent intent_MyToShare = new Intent(this, ShareActivity.class);
                startActivity(intent_MyToShare);
                Toast.makeText(this,"v_ll_share",Toast.LENGTH_SHORT).show();
                break;
            //设置
            case R.id.id_main_my_ll_set:
                Toast.makeText(this,"v_ll_set",Toast.LENGTH_SHORT).show();
                break;
            //关于
            case R.id.id_main_my_ll_about:
                Toast.makeText(this,"v_ll_about",Toast.LENGTH_SHORT).show();
                break;
            //清除缓存
            case R.id.id_main_my_ll_clear:
                Toast.makeText(this,"v_ll_clear",Toast.LENGTH_SHORT).show();
                break;
            //系统更新
            case R.id.id_main_my_ll_update:
                Toast.makeText(this,"v_ll_update",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
