package com.hkc.mymapy;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.baidu.mapapi.SDKInitializer;
import com.hkc.dao.RegistDao;

/**
 * Created by Administrator on 2016/7/18.
 */
public class MyMapApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);

//        RegistDao registDao = new RegistDao(this);
//        SQLiteDatabase readableDatabase = registDao.getReadableDatabase();
//        readableDatabase.close();
    }

}
