package com.hkc.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hkc.mymapy.R;

/**
 * Created by Administrator on 2016/8/4.
 */
public class Fragment_popup1  extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view_popup_frag1 = inflater.inflate(R.layout.framgment_popupwindow,container,false);
//        TextView textView = new TextView(getActivity());
//        textView.setText("asdasd");
        return view_popup_frag1;
    }
}
