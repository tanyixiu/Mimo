package com.tanyixiu.mimo.utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2015/7/18.
 */
public class MimoApp extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

    public static int getAppVersion() {
        try {

            PackageInfo info =
                    mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);

            return info.versionCode;

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }
        return 1;
    }

    public static File getDiskCacheDir(String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = mContext.getExternalCacheDir().getPath();
        } else {
            cachePath = mContext.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }
}
