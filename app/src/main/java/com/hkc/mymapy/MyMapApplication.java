package com.hkc.mymapy;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by Administrator on 2016/7/18.
 */
public class MyMapApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
    }

}
