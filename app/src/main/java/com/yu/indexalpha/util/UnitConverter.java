package com.yu.indexalpha.util;

import android.content.Context;
import android.util.DisplayMetrics;

public class UnitConverter {

    public static int dip2px(Context context, int dpValue) {
        //获取屏幕密度
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        //屏幕密度的比例值
        float density = displayMetrics.density;
        //将dp转换为px
        return (int) (dpValue * density + 0.5);
    }
}
