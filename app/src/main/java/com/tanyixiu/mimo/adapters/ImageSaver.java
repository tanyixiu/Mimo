package com.tanyixiu.mimo.adapters;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.tanyixiu.MimoApp;
import com.tanyixiu.mimo.R;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Mimo on 2015/9/11.
 */
public class ImageSaver {

    public static String saveBitmap(String uniqueName, Bitmap mBitmap) throws Exception {
        String dir = MimoApp.getSDCardDir() + File.separator + MimoApp.getContext().getResources().getString(R.string.app_name);
        File file = new File(dir, uniqueName + ".png");
        if (file.exists()) {
            file.delete();
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        file.createNewFile();
        FileOutputStream fOut = new FileOutputStream(file);
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        fOut.flush();
        fOut.close();
        return file.getPath();
    }

    public static void deleteBitmap(String url) {
        if (!TextUtils.isEmpty(url)) {
            File file = new File(url);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
