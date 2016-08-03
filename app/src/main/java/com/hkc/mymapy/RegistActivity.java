package com.hkc.mymapy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tv_back;
    private ImageView iv_getYanZheng,iv_resume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        tv_back = (TextView) findViewById(R.id.id_regist_back);
        iv_getYanZheng = (ImageView) findViewById(R.id.id_regist_getyanzheng);
        iv_resume = (ImageView) findViewById(R.id.id_regist_resume);

        tv_back.setOnClickListener(this);
        iv_getYanZheng.setOnClickListener(this);
        iv_resume.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回
            case R.id.id_regist_back:
                this.finish();
                break;
            //获取验证码
            case R.id.id_regist_getyanzheng:
                Toast.makeText(this,"短信已发至您手机",Toast.LENGTH_LONG).show();
                break;
            //提交
            case R.id.id_regist_resume:
                this.finish();
                break;
        }
    }
}
