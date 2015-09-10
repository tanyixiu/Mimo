package com.tanyixiu.mimo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mimo on 2015/9/10.
 */
public class StringHelper {

    public static String getCurrentTime() {
        long time = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(time));
    }
}
