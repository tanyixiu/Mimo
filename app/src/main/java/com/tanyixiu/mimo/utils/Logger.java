package com.tanyixiu.mimo.utils;

import android.util.Log;

/**
 * Created by tanyixiu on 2015/7/16.
 */
public class Logger {
    private static final String DEFAULT_TAG = "mimo";

    private static final int LEVEL_VERBOSE = 0;
    private static final int LEVEL_DEBUG = 1;
    private static final int LEVEL_INFO = 2;
    private static final int LEVEL_WARN = 3;
    private static final int LEVEL_ERROR = 4;
    private static final int LEVEL_NOTHING = 100;
    private static final int LEVEL_CURRENT = LEVEL_DEBUG;

    private static Logger logger;

    private Logger() {
    }

    public static Logger single() {
        if (logger == null) {
            synchronized (Logger.class) {
                if (logger == null) {
                    logger = new Logger();
                }
            }
        }
        return logger;
    }

    public void v(String msg) {
        v(DEFAULT_TAG, msg);
    }

    public void v(String tag, String msg) {
        if (LEVEL_VERBOSE >= LEVEL_CURRENT) {
            Log.d(tag, msg);
        }
    }

    public void d(String msg) {
        d(DEFAULT_TAG, msg);
    }

    public void d(String tag, String msg) {
        if (LEVEL_DEBUG >= LEVEL_CURRENT) {
            Log.d(tag, msg);
        }
    }


    public void i(String msg) {
        i(DEFAULT_TAG, msg);
    }

    public void i(String tag, String msg) {
        if (LEVEL_INFO >= LEVEL_CURRENT) {
            Log.d(tag, msg);
        }
    }


    public void w(String msg) {
        w(DEFAULT_TAG, msg);
    }

    public void w(String tag, String msg) {
        if (LEVEL_WARN >= LEVEL_CURRENT) {
            Log.d(tag, msg);
        }
    }


    public void e(String msg) {
        e(DEFAULT_TAG, msg);
    }

    public void e(String tag, String msg) {
        if (LEVEL_ERROR >= LEVEL_CURRENT) {
            Log.d(tag, msg);
        }
    }

}
