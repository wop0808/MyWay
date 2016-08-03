package com.hkc.mymapy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.hkc.mymapy.R.layout.activity_share);

        iv_back = (ImageView) findViewById(R.id.id_share_back);

        iv_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //返回
            case R.id.id_share_back:
                this.finish();
                break;
        }
    }
}
