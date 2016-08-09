package com.hkc.mymapy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkc.dao.RegistDao;
import com.hkc.utitls.IsArraysEmpty;

import java.util.ArrayList;
import java.util.Collections;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    private final int RESULT_MYtoLOGIN = 1;
    private String TAG = "crazyK";
    private TextView tv_findPwd;
    private ImageView tv_regist,tv_login,tv_back;
    private AutoCompleteTextView act_username;
    //密码控件(后面的et_Psw是控件内的内容)
    private EditText et_psw;
    private String[] mUserNames;
    private String[] mUserPsws;
    private RegistDao registDao;
    private Context context;
    //账号密码由数组→结合
    private ArrayList<String> userNameLists;
    private ArrayList<String> pswLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registDao = new RegistDao(this);
        context = this;

        tv_regist = (ImageView) findViewById(R.id.id_login_regist);
        tv_back = (ImageView) findViewById(R.id.id_login_back);
        tv_login = (ImageView) findViewById(R.id.id_login_login);
        tv_findPwd = (TextView) findViewById(R.id.id_login_findpwd);
        act_username= (AutoCompleteTextView) findViewById(R.id.id_login_act_username);
        et_psw = (EditText) findViewById(R.id.id_login_psw );

        tv_regist.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        tv_findPwd.setOnClickListener(this);
        act_username.setOnItemClickListener(this);
        act_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

//                Log.i(TAG, "act 改变监听");
                String[][] mUserInfoDatas = registDao.startRead(registDao);
                mUserNames = mUserInfoDatas[0];
                mUserPsws = mUserInfoDatas[1];
//                Log.i(TAG, ""+mUserNames[0] +"------" + mUserPsws[0]);
//                Log.i(TAG, "act监听获得数据：" + mUserNames +"---------" +mUserPsws);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,mUserNames);
                act_username.setAdapter(arrayAdapter);
//                Log.i(TAG, "绑定适配器");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
                String act_UserName = act_username.getText().toString().trim();
                String et_Psw = et_psw.getText().toString().trim();
                //将数据库获得的账号密码 从数组-->集合
                if( !IsArraysEmpty.isArrayEmpty(mUserNames) && !IsArraysEmpty.isArrayEmpty(mUserPsws)){
                    userNameLists = new ArrayList<>();
                    for(int i = 0 ;i < mUserNames.length  ;i++ ){
                        userNameLists.add(mUserNames[i]);
                    }
                    pswLists = new ArrayList<>();
                    for(int i = 0 ; i < mUserPsws.length ; i++ ){
                        pswLists.add(mUserPsws[i]);
                    }
                    //用集合形式的账号密码进行验证登录
                    if (userNameLists.contains(act_UserName) && pswLists.contains(et_Psw) && userNameLists != null &&  pswLists != null &&  userNameLists.indexOf(act_UserName) == pswLists.indexOf(et_Psw)) {
                        Intent intent_LoginToMy = new Intent();
                        intent_LoginToMy.putExtra("act_UserName",act_UserName);
                        setResult(RESULT_MYtoLOGIN,intent_LoginToMy);

                        this.finish();
                    }
                }


                break;
            //忘记密码
            case R.id.id_login_findpwd:
                Intent intent_LoginToFindPwd = new Intent(this,FindPwdActivity.class);
                startActivity(intent_LoginToFindPwd);
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        et_psw.setText(mUserPsws[position]);
        String ID = (String) parent.getItemAtPosition(position);
        String psw = registDao.getPswByID(registDao,ID);
        et_psw.setText(psw);

    }
}
