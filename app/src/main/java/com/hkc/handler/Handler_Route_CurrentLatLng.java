package com.hkc.handler;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.hkc.listener.OnGetCurrentLalngListener;

/**
 * Created by Administrator on 2016/8/17.
 */
public class Handler_Route_CurrentLatLng extends Handler {
    private String TAG = "crazyK";
    private OnGetCurrentLalngListener onGetCurrentLalngListener;

    public Handler_Route_CurrentLatLng(OnGetCurrentLalngListener onGetCurrentLalngListener) {
        this.onGetCurrentLalngListener = onGetCurrentLalngListener;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == 1) {
            BDLocation bdLocation = (BDLocation) msg.obj;
            onGetCurrentLalngListener.startSearch(bdLocation);
        } else {
            Log.i(TAG, "mse传输错误 ");
        }
    }
}
