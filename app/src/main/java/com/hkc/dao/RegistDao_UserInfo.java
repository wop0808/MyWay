package com.hkc.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2016/8/5.
 * 用于账号密码数据库的建立，及数据查询等功能
 * 逻辑不够完善，不知道如何判断数据库内账号ID 不重复问题
 */
public class RegistDao_UserInfo extends SQLiteOpenHelper {
    public static int mDB_UserInfo_Version = 1;
    public static String mDB_UserInfo_Name = "UserInfo.db";
    public static String mDB_UserInfo_TableName = "UserInfo";
    public static String mUserInfo_username = "username";
    public static String mUserInfo_psw = "psw";
    private Context contextRead;
    private Context contextWrite;
    private String userName;
    private String psw;
    private String TAG = "crazyK";

    //用于读数据库的构造函数
    public RegistDao_UserInfo(Context contextRead) {
        super(contextRead, mDB_UserInfo_Name, null, mDB_UserInfo_Version);
        this.contextRead = contextRead;
    }

    //用于写数据库的构造函数
    public RegistDao_UserInfo(Context contextWrite, String userName, String psw) {
        super(contextWrite, mDB_UserInfo_Name, null, mDB_UserInfo_Version);
        this.contextWrite = contextWrite;
        this.userName = userName;
        this.psw = psw;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table UserInfo(_id integer primary key autoincrement ,username String ,psw String )");
        Log.i(TAG, "创建数据库成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //以二维数组的形式返回读取到的账号密码
    public String[][] startRead(RegistDao_UserInfo registDaoUserInfo) {
        Log.i(TAG, "开始读取数据库");
        //获得可读权限
        SQLiteDatabase readableDatabase = registDaoUserInfo.getReadableDatabase();
        Cursor cursor = readableDatabase.query(RegistDao_UserInfo.mDB_UserInfo_TableName, null, null, null, null, null, null, null);
        //接收用户名
        String[] userNames = new String[cursor.getCount()];
        //接收密码
        String[] psws = new String[cursor.getCount()];
        //设置角标
        int x = 0;
        int y = 0;
        if (cursor.moveToFirst()) {
            do {
//                Log.i(TAG, "y游标读取数据");
                userNames[x++] = cursor.getString(cursor.getColumnIndex(RegistDao_UserInfo.mUserInfo_username));
                psws[y++] = cursor.getString(cursor.getColumnIndex(RegistDao_UserInfo.mUserInfo_psw));

            } while (cursor.moveToNext());
        }
//        Log.i(TAG, userNames.toString() + "------------------" + psws.toString());
        if (readableDatabase.isOpen()) {
            readableDatabase.close();
        }
        //第一排为账号，第二排密码
        String[][] datas = new String[2][];
        datas[0] = userNames;
        datas[1] = psws;
        return datas;
    }

    //写入账号密码
    public void startWrite(RegistDao_UserInfo registDaoUserInfo) {
        SQLiteDatabase writableDatabase = registDaoUserInfo.getWritableDatabase();

        ContentValues values = new ContentValues();
        Log.i(TAG, "开始写入数据：" + userName + "---------" + psw);
        values.put("username", userName);
        values.put("psw", psw);
        writableDatabase.insert(RegistDao_UserInfo.mDB_UserInfo_TableName, "nullColumn", values);

        if (writableDatabase.isOpen()) {
            writableDatabase.close();
        }

        Log.i(TAG, "数据写入完成：" + userName + "---------" + psw);
    }

    //根据指定账户名查询密码
    public String getPswByID(RegistDao_UserInfo registDaoUserInfo, String ID){
        SQLiteDatabase readableDatabase = registDaoUserInfo.getReadableDatabase();
        Cursor cursor = readableDatabase.query(RegistDao_UserInfo.mDB_UserInfo_TableName, new String[]{mUserInfo_psw}, mUserInfo_username+ "= ?", new String[]{ID}, null, null, null);
        String pswByID ="";
        if(cursor.moveToFirst()){
            do{
                pswByID = cursor.getString(cursor.getColumnIndex(mUserInfo_psw));
            }while (cursor.moveToNext());
        }
        return  pswByID;
    }

}