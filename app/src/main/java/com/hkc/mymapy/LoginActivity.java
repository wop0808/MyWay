package com.hkc.mymapy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tv_findPwd;
    private ImageView tv_regist,tv_login,tv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tv_regist = (ImageView) findViewById(R.id.id_login_regist);
        tv_back = (ImageView) findViewById(R.id.id_login_back);
        tv_login = (ImageView) findViewById(R.id.id_login_login);
        tv_findPwd = (TextView) findViewById(R.id.id_login_findpwd);

        tv_regist.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        tv_findPwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //注册
            case R.id.id_login_regist:
                Intent intent_LoginToRegist = new Intent(LoginActivity.this,RegistActivity.class);
                startActivity(intent_LoginToRegist);
                break;
            //返回
            case R.id.id_login_back:
                this.finish();
                break;
            //登录
            case R.id.id_login_login:

                this.finish();
                break;
            //忘记密码
            case R.id.id_login_findpwd:
                Intent intent_LoginToFindPwd = new Intent(this,FindPwdActivity.class);
                startActivity(intent_LoginToFindPwd);
                break;
        }
    }
}
