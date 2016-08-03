package com.hkc.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hkc.adapter.Collect_point_Adapter;
import com.hkc.mymapy.R;

/**
 * Created by Administrator on 2016/7/13.
 */
public class Fragment_collect_point extends Fragment {
    private ListView listView;
    private Collect_point_Adapter collect_point_adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v_collect_point_fragment = inflater.inflate(R.layout.fragment_collect_point,container,false);

        collect_point_adapter = new Collect_point_Adapter(this.getActivity());

        listView = (ListView) v_collect_point_fragment.findViewById(R.id.id_collect_fragment_point_lv);
        listView.setAdapter(collect_point_adapter);



        return v_collect_point_fragment;
    }
}
