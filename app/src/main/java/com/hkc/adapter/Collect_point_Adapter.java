package com.hkc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hkc.infomation.ViewHolder;
import com.hkc.infomation.ViewHolder_collect_point;
import com.hkc.mymapy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/7/13.
 */
public class Collect_point_Adapter extends BaseAdapter {
    private Context context;

    List<HashMap<String , Object>> collect_point_data = new ArrayList<>();

    public Collect_point_Adapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return collect_point_data.size();
    }

    @Override
    public Object getItem(int position) {
        return collect_point_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder_collect_point viewHolder_collect_point;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.listitem_collect_point,null);
            viewHolder_collect_point = new ViewHolder_collect_point();
            viewHolder_collect_point.tv_title = (TextView) convertView.findViewById(R.id.id_collect_fragment_item_title);
            viewHolder_collect_point.tv_detail = (TextView) convertView.findViewById(R.id.id_collect_fragment_item_detail);
            convertView.setTag(viewHolder_collect_point);
        }else {
            viewHolder_collect_point = (ViewHolder_collect_point) convertView.getTag();
        }


        return convertView;
    }
}
