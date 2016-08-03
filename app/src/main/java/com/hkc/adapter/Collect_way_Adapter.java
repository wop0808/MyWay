package com.hkc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hkc.infomation.ViewHolder_collect_way;
import com.hkc.mymapy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/7/13.
 */
public class Collect_way_Adapter extends BaseAdapter {
    private Context context;
    private List<HashMap<String, Object>> collect_way_data = new ArrayList<>();

    public Collect_way_Adapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return collect_way_data.size();
    }

    @Override
    public Object getItem(int position) {
        return collect_way_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder_collect_way viewHolder_collect_way;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.listitem_collect_way,null);
            viewHolder_collect_way = new ViewHolder_collect_way();


            convertView.setTag(viewHolder_collect_way);
        }else {
            viewHolder_collect_way = (ViewHolder_collect_way) convertView.getTag();
        }

        return convertView;
    }
}
