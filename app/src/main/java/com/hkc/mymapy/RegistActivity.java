package com.hkc.mymapy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hkc.dao.RegistDao;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tv_back;
    private ImageView iv_getYanZheng,iv_resume;
    private EditText et_userName,et_psw,et_repsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        tv_back = (TextView) findViewById(R.id.id_regist_back);
//        iv_getYanZheng = (ImageView) findViewById(R.id.id_regist_getyanzheng);
        iv_resume = (ImageView) findViewById(R.id.id_regist_resume);
        et_psw = (EditText) findViewById(R.id.id_regist_psw);
        et_repsw = (EditText) findViewById(R.id.id_regist_repsw);
        et_userName = (EditText) findViewById(R.id.id_regist_username);

        tv_back.setOnClickListener(this);
//        iv_getYanZheng.setOnClickListener(this);
        iv_resume.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回
            case R.id.id_regist_back:
                this.finish();
                break;
//            //获取验证码
//            case R.id.id_regist_getyanzheng:
//                Toast.makeText(this,"短信已发至您手机",Toast.LENGTH_LONG).show();
//                break;
            //提交
            case R.id.id_regist_resume:
                String userName = et_userName.getText().toString().trim();
                String psw = et_psw.getText().toString().trim();
                String repsw = et_repsw.getText().toString().trim();
                //开始验证
                if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(psw)){
                    Toast.makeText(RegistActivity.this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!TextUtils.equals(psw,repsw)){
                    Toast.makeText(RegistActivity.this, "两次密码不一致,请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isEmpty(userName) && TextUtils.equals(psw,repsw)){
                    Toast.makeText(RegistActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    RegistDao registDao = new RegistDao(this,userName,psw);
                    registDao.startWrite(registDao);
                    this.finish();
                }
                break;
        }
    }
}
