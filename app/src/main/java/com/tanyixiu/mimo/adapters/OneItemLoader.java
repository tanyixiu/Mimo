package com.tanyixiu.mimo.adapters;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.activeandroid.query.Select;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tanyixiu.mimo.activities.MainActivity;
import com.tanyixiu.mimo.moduls.OneItem;
import com.tanyixiu.mimo.utils.OneItemParser;
import com.tanyixiu.mimo.utils.ToastUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mimo on 2015/9/10.
 */
public class OneItemLoader {
    private static final String STR_EARLIEST_DATE = "2012-10-07";
    private static final String STR_ONEITEM_URL = "http://caodan.org/%s-photo.html";
    private static final String STR_ONEIMAGE_URL = "http://caodan.org/wp-content/uploads/vol/%s.jpg";

    public interface OnOneItemLoadedListener {
        void onLoaded(OneItem oneItem);
    }

    public static int getMaxOneItemID() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date earliestDate;
        try {
            earliestDate = sdf.parse(STR_EARLIEST_DATE);
            Date currentDate = new Date();
            long timesSpan = currentDate.getTime() - earliestDate.getTime();
            long days = TimeUnit.MILLISECONDS.toDays(timesSpan);
            return Math.round(days);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getOneItemUrlById(int oneId) {
        return String.format(STR_ONEITEM_URL, oneId);
    }

    public static String getOneImageUrlById(int oneId) {
        return String.format(STR_ONEIMAGE_URL, oneId);
    }

    public void loadOneItem(final int id, final OnOneItemLoadedListener listener) {
        OneItemLoaderThread thread = new OneItemLoaderThread(id, listener);
        thread.start();
    }

    class OneItemLoaderThread extends Thread {

        private int mId;
        private OnOneItemLoadedListener mListener;

        public OneItemLoaderThread(int id, final OnOneItemLoadedListener listener) {
            mId = id;
            mListener = listener;
        }

        @Override
        public void run() {
            super.run();
            Looper.prepare();
            loadOneItem();
            Looper.loop();
        }

        private Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (null != msg) {
                    OneItem oneItem = null;
                    if (null != msg.obj) {
                        oneItem = (OneItem) msg.obj;
                    } else {
                        ToastUtils.showLong("Loading Failed");
                    }
                    mListener.onLoaded(oneItem);
                }
            }
        };

        private void sendMessage(OneItem oneItem) {
            Message message = new Message();
            message.what = 1;
            message.obj = oneItem;
            mHandler.sendMessage(message);
        }

        private void loadOneItem() {
            OneItem oneItem = getOneItemFromCache(mId);
            if (null != oneItem) {
                sendMessage(oneItem);
                return;
            }
            getOneItemFromServer(mId);
        }

        private OneItem getOneItemFromCache(int id) {
            OneItem oneItem = new Select().from(OneItem.class).where("id=?", id).executeSingle();
            if (null != oneItem) {
                return oneItem;
            }
            return null;
        }

        private void getOneItemFromServer(int id) {
            String oneItemUrl = getOneItemUrlById(id);
            StringRequest request = new StringRequest(oneItemUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        OneItem oneItem = OneItemParser.parserOne(s);
                        oneItem.save();
                        sendMessage(oneItem);
                    } catch (Exception e) {
                        e.printStackTrace();
                        sendMessage(null);

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyError.printStackTrace();
                    sendMessage(null);
                }
            });
            MainActivity.getRequestQueue().add(request);
        }

    }

}