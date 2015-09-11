package com.tanyixiu.mimo.adapters;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.activeandroid.query.Select;
import com.tanyixiu.mimo.moduls.BookItem;

import java.util.List;

/**
 * Created by Mimo on 2015/9/11.
 */
public class BookItemLoader {

    public interface BookItemListener {

    }

    public interface OnBookItemLoadedListener extends BookItemListener {
        void onLoaded(List<BookItem> bookItems);
    }

    public interface OnBookItemSaveListener extends BookItemListener {
        void onSaved(boolean success);
    }

    public interface OnBookItemDeleteListener extends BookItemListener {
        void onDeleted(boolean success);
    }

    public void loadBookItems(BookItemListener listener) {
        BookItemLoaderThread thread = new BookItemLoaderThread(listener, MessageType.LOAD_BOOK_ITEMS);
        thread.start();
    }

    public void saveBookItem(BookItem bookItem, Bitmap bitmap, BookItemListener listener) {
        BookItemLoaderThread thread = new BookItemLoaderThread(listener, MessageType.SAVE_BOOK_ITEM, bookItem, bitmap);
        thread.start();
    }

    public void deleteBookItem(BookItem bookItem, BookItemListener listener) {
        BookItemLoaderThread thread = new BookItemLoaderThread(listener, MessageType.DELETE_BOOK_ITEM, bookItem);
        thread.start();
    }

    class MessageType {
        public static final int LOAD_BOOK_ITEMS = 0;
        public static final int SAVE_BOOK_ITEM = 1;
        public static final int DELETE_BOOK_ITEM = 2;
    }

    class BookItemLoaderThread extends Thread {


        private Object[] mParams;
        private BookItemListener mListener;
        private int msgType = MessageType.LOAD_BOOK_ITEMS;

        public BookItemLoaderThread(BookItemListener listener, int msgType, Object... params) {
            this.msgType = msgType;
            this.mListener = listener;
            this.mParams = params;
        }

        @Override
        public void run() {
            super.run();
            Looper.prepare();
            switch (msgType) {
                case MessageType.LOAD_BOOK_ITEMS:
                    loadThinkingItem();
                    break;
                case MessageType.SAVE_BOOK_ITEM:
                    saveBookItem();
                    break;
                case MessageType.DELETE_BOOK_ITEM:
                    deleteBookItem();
                    break;
                default:
                    break;
            }
            Looper.loop();
        }

        private Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MessageType.LOAD_BOOK_ITEMS:
                        ((OnBookItemLoadedListener) mListener).onLoaded((List<BookItem>) msg.obj);
                        break;
                    case MessageType.SAVE_BOOK_ITEM:
                        ((OnBookItemSaveListener) mListener).onSaved((Boolean) msg.obj);
                        break;
                    case MessageType.DELETE_BOOK_ITEM:
                        ((OnBookItemDeleteListener) mListener).onDeleted((Boolean) msg.obj);
                        break;
                    default:
                        break;
                }

            }
        };

        private void loadThinkingItem() {
            List<BookItem> items = loadThinkingItemFromCache();
            Message message = new Message();
            message.what = MessageType.LOAD_BOOK_ITEMS;
            message.obj = items;
            mHandler.sendMessage(message);
        }

        private void saveBookItem() {
            Message message = new Message();
            message.what = MessageType.SAVE_BOOK_ITEM;
            message.obj = true;
            if (null != mParams && 0 != mParams.length) {
                BookItem item = (BookItem) mParams[0];

                Bitmap bitmap = (Bitmap) mParams[1];
                if (null != bitmap) {
                    try {
                        String path = ImageSaver.saveBitmap(item.getId(), bitmap);
                        path = "file:/" + path;
                        item.setCoverUrl(path);
                    } catch (Exception e) {
                        message.obj = false;
                        e.printStackTrace();
                    }
                }
                item.save();
            }

            mHandler.sendMessage(message);
        }

        private void deleteBookItem() {
            Message message = new Message();
            message.what = MessageType.DELETE_BOOK_ITEM;
            message.obj = true;
            if (null != mParams && 0 != mParams.length) {
                BookItem item = (BookItem) mParams[0];
                item.delete();
                String url = item.getCoverUrl();
                ImageSaver.deleteBitmap(url);
            }
            mHandler.sendMessage(message);
        }

        private List<BookItem> loadThinkingItemFromCache() {
            return new Select().from(BookItem.class).orderBy("state").execute();
        }
    }

}
