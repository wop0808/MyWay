package com.hkc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hkc.infomation.ViewHolder;
import com.hkc.infomation.WayInfo;
import com.hkc.mymapy.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/6.
 */
public class WayAdapter extends BaseAdapter {

    private ArrayList<WayInfo> wayInfoArrayList ;
    private Context context;

    public WayAdapter(Context context){
        this.context =context;
        wayInfoArrayList = new ArrayList<>();
    }


    /**
     * 重新设置数据
     * @param wayInfoArrayList
     */
    public void setData(ArrayList<WayInfo> wayInfoArrayList){
        this.wayInfoArrayList.clear();
        this.wayInfoArrayList.addAll(wayInfoArrayList);
        this.notifyDataSetChanged();
    }

    public void addData(){

    }

    @Override
    public int getCount() {
        return wayInfoArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return wayInfoArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = convertView.inflate(context, R.layout.way_listitem,null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.id_main_way_tv_remenber);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }
}
