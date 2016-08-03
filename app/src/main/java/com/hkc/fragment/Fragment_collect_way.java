package com.hkc.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hkc.adapter.Collect_way_Adapter;
import com.hkc.mymapy.R;

/**
 * Created by Administrator on 2016/7/13.
 */
public class Fragment_collect_way extends Fragment {
    private ListView listView;
    private Collect_way_Adapter collect_way_adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v_collect_way_fragment = inflater.inflate(R.layout.fragment_collect_way,container,false);

        listView = (ListView) v_collect_way_fragment.findViewById(R.id.id_collect_fragment_way_lv);

        collect_way_adapter = new Collect_way_Adapter(this.getActivity());
        listView.setAdapter(collect_way_adapter);

        return v_collect_way_fragment;
    }
}
