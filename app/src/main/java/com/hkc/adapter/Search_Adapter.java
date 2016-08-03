package com.hkc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hkc.infomation.ViewHolder_Search;
import com.hkc.mymapy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/7/13.
 */
public class Search_Adapter extends BaseAdapter {
    private List<HashMap<String ,Object>> search_list_data = new ArrayList<>();
    private Context context;

    @Override
    public int getCount() {
        return search_list_data.size();
    }

    @Override
    public Object getItem(int position) {
        return search_list_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder_Search viewHolder_search ;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.listitem_search,null);
            viewHolder_search = new ViewHolder_Search();

            convertView.setTag(viewHolder_search);
        }else {
            viewHolder_search = (ViewHolder_Search) convertView.getTag();
        }
        return convertView;
    }
}
