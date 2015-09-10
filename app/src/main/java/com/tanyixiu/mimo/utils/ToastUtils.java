package com.tanyixiu.mimo.utils;

import android.widget.Toast;

import com.tanyixiu.MimoApp;

public class ToastUtils {

    public static void showLong(String message) {
        Toast.makeText(MimoApp.getContext(), message, Toast.LENGTH_LONG).show();
    }

    public static void showLong(int resId) {
        Toast.makeText(MimoApp.getContext(), resId, Toast.LENGTH_LONG).show();
    }
}
