package com.hkc.view;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hkc.mymapy.R;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2016/8/17.
 */
public class DriverRouteLinePlan extends LinearLayout {

    TextView tv_type, tv_time, tv_distance;

    public DriverRouteLinePlan(Context context) {
        super(context);
        setOrientation(VERTICAL);
        View drivingRoutePlan_Info = View.inflate(context, R.layout.view_driver_route_line_plan, this);
        tv_type = (TextView) drivingRoutePlan_Info.findViewById(R.id.id_drivingrouteplan_info_type);
        tv_time = (TextView) drivingRoutePlan_Info.findViewById(R.id.id_drivingrouteplan_info_time);
        tv_distance = (TextView) drivingRoutePlan_Info.findViewById(R.id.id_drivingrouteplan_info_distance);
    }

    public void setType(String type) {
        this.tv_type.setText(type);
    }

    public void setTime(int time) {
        //TODO 将毫秒时间转换成便于阅读的时间
        int minute = time/60;
        tv_time.setText(minute + "分钟");
    }

    public void setDistance(float distance) {
        //TODO 将路程转换成公里数
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        float kilometer = distance/1000;
        String formatKilometer = decimalFormat.format(kilometer);
        tv_distance.setText(formatKilometer + "公里");
    }


//    @Override
//    public View getChildAt(int index) {
//        LinearLayout childAt = (LinearLayout)super.getChildAt(0);
//        return childAt.getChildAt(index);
//    }
}
