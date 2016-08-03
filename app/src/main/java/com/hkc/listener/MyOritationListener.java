package com.hkc.listener;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Administrator on 2016/7/21.
 */
public class MyOritationListener implements SensorEventListener {
    private SensorManager sensorManager;
    private Context context;
    private Sensor sensor;

    private float lastX;
    private OnOritationListener onOritationListener;



    public MyOritationListener(Context context){
        this.context = context;
    }

    //开始监听
    public void start(){
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            //获得方向传感器
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
        if (sensor != null) {
            sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_UI);
        }
    }
    //停止监听
    public void stop(){
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ORIENTATION){
            //获得x度数
            float x = event.values[SensorManager.DATA_X];
            //如果角度大于1 则更新数据
            if (Math.abs(x - lastX ) > 1.0){
                if (onOritationListener != null) {
                    onOritationListener.onOritationChanged(x);
                }
            }

            lastX = x;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface OnOritationListener{
        void onOritationChanged(float x);
    }

    public void setOnOritationListener(OnOritationListener onOritationListener) {
        this.onOritationListener = onOritationListener;
    }
}
