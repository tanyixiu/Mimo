package com.tanyixiu.mimo.utils;

import android.util.TypedValue;

import com.tanyixiu.MimoApp;

/**
 * Created by Mimo on 2015/9/11.
 */
public class ToolUtils {
    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                MimoApp.getContext().getResources().getDisplayMetrics());
    }
}
