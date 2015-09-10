package com.tanyixiu.mimo.adapters;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.activeandroid.query.Select;
import com.tanyixiu.mimo.moduls.ThinkingItem;

import java.util.List;

/**
 * Created by Mimo on 2015/9/10.
 */
public class ThinkingItemLoader {

    public interface OnThinkingItemLoadedListener {
        void onLoaded(List<ThinkingItem> thinkingItems);
    }

    public void loadThinkItems(OnThinkingItemLoadedListener listener) {
        ThinkItemLoaderThread thread = new ThinkItemLoaderThread(listener);
        thread.start();
    }

    class ThinkItemLoaderThread extends Thread {

        private OnThinkingItemLoadedListener mListener;

        public ThinkItemLoaderThread(OnThinkingItemLoadedListener listener) {
            mListener = listener;
        }

        @Override
        public void run() {
            super.run();
            Looper.prepare();
            loadThinkingItem();
            Looper.loop();
        }

        private Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mListener.onLoaded((List<ThinkingItem>) msg.obj);
            }
        };

        private void sendMessage(List<ThinkingItem> items) {
            Message message = new Message();
            message.what = 1;
            message.obj = items;
            mHandler.sendMessage(message);
        }

        private void loadThinkingItem() {
            List<ThinkingItem> items = loadThinkingItemFromCache();
            sendMessage(items);
        }

        private List<ThinkingItem> loadThinkingItemFromCache() {
            return new Select().from(ThinkingItem.class).orderBy("createtime desc").execute();
        }
    }
}
