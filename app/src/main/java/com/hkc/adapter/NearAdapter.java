package com.hkc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hkc.mymapy.R;
import com.hkc.infomation.ViewHolder;

/**
 * Created by Administrator on 2016/6/27.
 */

public class NearAdapter extends BaseAdapter {

    private Context context;

    int[] imgID = new int[]{
            R.mipmap.near_listitem_foodpic, R.mipmap.near_listitem_traval, R.mipmap.near_listitem_bank,
            R.mipmap.near_listitem_relax, R.mipmap.near_listitem_wcpic};

    public NearAdapter(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imgID.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return imgID[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder ;
        if(convertView == null){
            convertView =convertView.inflate(this.context,R.layout.near_listitem, null);
            viewHolder = new ViewHolder();
            viewHolder.imgV = (ImageView) convertView.findViewById(R.id.id_near_listitem_imgv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imgV.setImageResource(imgID[position]);
        return convertView;
    }

}
