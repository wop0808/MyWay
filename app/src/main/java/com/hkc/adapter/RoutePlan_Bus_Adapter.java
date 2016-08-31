package com.hkc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hkc.mymapy.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/31.
 */
public class RoutePlan_Bus_Adapter extends BaseAdapter {
    private ArrayList<String> stringArrayList;
    private Context context;

    public RoutePlan_Bus_Adapter(ArrayList<String> stringArrayList , Context context){
        this.context = context;
        this.stringArrayList = stringArrayList;
    }

    @Override
    public int getCount() {
        return stringArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return stringArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem_routeplan_bus,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.id_routeplan_listitem_tv_title);
            viewHolder.tv_detail = (TextView) convertView.findViewById(R.id.id_routeplan_listitem_tv_detail);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_title.setText("方案"+(position+1));
        viewHolder.tv_detail.setText(stringArrayList.get(position));

        return convertView;
    }

    private class ViewHolder{
        private TextView tv_title,tv_detail;
    }
}
