package com.hkc.utitls;

import android.app.Activity;

/**
 * Created by Administrator on 2016/7/6.
 */
public class ScreenUtils {
    public static int screenHeight(Activity context){
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        return h;
    }

    public static int screenWidth(Activity context){
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        return w;
    }
}
