package com.tanyixiu.mimo.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.LruCache;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tanyixiu.mimo.main.MainActivity;
import com.tanyixiu.mimo.performance.DiskLruCache;
import com.tanyixiu.mimo.resolver.OneResolver;
import com.tanyixiu.mimo.utils.Encrypter;
import com.tanyixiu.mimo.utils.OneItemParser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by tanyixiu on 2015/7/16.
 */
public class OneItemLoader {

    private static final int PAGE_SIZE = 5;
    private static final String EARLIESTDATE = "2012-10-07";

    private Context mContext;
    private Set<Integer> mRequestIds;
    private Set<Integer> mRequestImgs;
    private Set<BitmapWorkerTask> taskCollection;
    private DiskLruCache mDiskLruCache;
    private LruCache<String, Bitmap> mBitmapMemoryLruCache;
    private LruCache<Integer, OneItemEntity> mOneItemMemoryCache;

    public interface OnOneItemLoadedListener {
        void onLoaded(List<Integer> idList);
    }

    public OneItemLoader(Context context) {
        mContext = context;
        mRequestIds = new HashSet<>();
        mRequestImgs = new HashSet<>();
        mDiskLruCache = DiskLruCache.getInstance();
        taskCollection = new HashSet<>();

        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        mBitmapMemoryLruCache = new LruCache<String, Bitmap>(maxMemory / 8) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
        mOneItemMemoryCache = new LruCache<Integer, OneItemEntity>(maxMemory / 16) {
            @Override
            protected int sizeOf(Integer key, OneItemEntity value) {

                return super.sizeOf(key, value);
            }
        };
    }

    private void putOneItemToCache(int oneItemId, OneItemEntity entity) {
        mOneItemMemoryCache.put(oneItemId, entity);
    }

    private void putBitmapToCache(String imgUrl, Bitmap bitmap) {
        mBitmapMemoryLruCache.put(imgUrl, bitmap);
    }

    private OneItemEntity getOneItemFromCache(int oneItemId) {
        return mOneItemMemoryCache.get(oneItemId);
    }

    public Bitmap getBitmapFromCache(String imgUrl) {
        return mBitmapMemoryLruCache.get(imgUrl);
    }

    private List<Integer> getOneItemIds(int startOneItemId, int pagesize) {
        if (0 == startOneItemId) {
            return null;
        }
        List<Integer> idList = new ArrayList<>();
        for (int i = startOneItemId; i > (startOneItemId - pagesize); i--) {
            idList.add(i);
        }
        return idList;
    }

    public OneItemEntity getOneItem(int oneItemId) {
        OneItemEntity entity = getOneItemFromCache(oneItemId);
        if (null != entity) {
            return entity;
        }
        entity = OneResolver.readOneItemByIdFromDb(mContext, oneItemId);
        if (null != entity) {
            putOneItemToCache(oneItemId, entity);
            return entity;
        }
        return null;
    }

    public Bitmap getBitmap(String imgUrl) {
        Bitmap bitmap = getBitmapFromCache(imgUrl);
        if (null != bitmap) {
            return bitmap;
        }
        FileDescriptor fileDescriptor = null;
        FileInputStream fileInputStream = null;
        try {
            final String key = Encrypter.encryptByMD5(imgUrl);
            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
            if (snapShot != null) {
                fileInputStream = (FileInputStream) snapShot.getInputStream(0);
                fileDescriptor = fileInputStream.getFD();
            }
            if (fileDescriptor != null) {
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            }
            if (bitmap != null) {
                putBitmapToCache(imgUrl, bitmap);
            }
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileDescriptor == null && fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public void loadCurrentPageOfOneItems(int startOneItemId, int itemCount, final OnOneItemLoadedListener listener) {
        loadOneItems(startOneItemId, itemCount, listener);
    }


    public void loadPageOfOneItems(int startOneItemId, final OnOneItemLoadedListener listener) {
        loadOneItems(startOneItemId, PAGE_SIZE, listener);
    }

    private void loadOneItems(int startOneItemId, int pageSize, final OnOneItemLoadedListener listener) {
        final List<Integer> idList = getOneItemIds(startOneItemId, pageSize);
        if (null == idList || 0 == idList.size()) {
            listener.onLoaded(idList);
            return;
        }
        for (final int oneItemId : idList) {
            OneItemEntity entity = getOneItem(oneItemId);
            if (null == entity) {
                mRequestIds.add(oneItemId);
            }

            String imgUrl = getOneImageUrlById(oneItemId);
            Bitmap bitmap = getBitmap(imgUrl);
            if (null == bitmap) {
                mRequestImgs.add(oneItemId);
            }
        }

        if (0 == mRequestIds.size() && 0 == mRequestImgs.size()) {
            listener.onLoaded(idList);
        }

        for (int id : mRequestIds) {
            StringRequest request = getStringRequest(listener, idList, id);
            MainActivity.getRequestQueue().add(request);
        }
        for (int id : mRequestImgs) {
            String imgUrl = getOneImageUrlById(id);
            BitmapWorkerTask task = new BitmapWorkerTask(id, listener, idList);
            taskCollection.add(task);
            task.execute(imgUrl);
        }
    }

    private StringRequest getStringRequest(final OnOneItemLoadedListener listener, final List<Integer> idList, final int oneItemId) {
        String oneItemUrl = getOneItemUrlById(oneItemId);
        return new StringRequest(oneItemUrl,
                new Listener<String>() {
                    @Override
                    public void onResponse(String html) {
                        try {
                            if (TextUtils.isEmpty(html)) {
                                throw new Exception("request OneItem failed");
                            }
                            OneItemEntity entity = OneItemParser.parser(html);
                            if (null == entity) {
                                throw new Exception("OneItemParser parsed failed");
                            }
                            putOneItemToCache(entity.getId(), entity);
                            OneResolver.saveOneItemToDb(mContext, entity);
                            mRequestIds.remove(oneItemId);
                            if (0 == mRequestIds.size() && 0 == mRequestImgs.size()) {
                                listener.onLoaded(idList);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                    }
                });
    }

    public void flushCache() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancelAllTasks() {
        if (taskCollection != null) {
            for (BitmapWorkerTask task : taskCollection) {
                task.cancel(false);
            }
        }
    }

    public int getIdByDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date oldestDate;
        try {
            oldestDate = sdf.parse(EARLIESTDATE);
        } catch (ParseException e) {
            return 1;
        }
        if (date.before(oldestDate)) {
            return 1;
        }
        Date currentDate = new Date();
        if (date.after(currentDate)) {
            date = currentDate;
        }
        long timesSpan = date.getTime() - oldestDate.getTime();
        long days = TimeUnit.MILLISECONDS.toDays(timesSpan);
        return Math.round(days);
    }

    public int getMaxOneItemID() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date earliestDate;
        try {
            earliestDate = sdf.parse(EARLIESTDATE);
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
        return "http://caodan.org/" + oneId + "-photo.html";
    }

    public String getOneImageUrlById(int oneId) {
        return "http://caodan.org/wp-content/uploads/vol/" + oneId + ".jpg";
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

        private String imageUrl;
        private final OnOneItemLoadedListener listener;
        private final List<Integer> idList;
        private final int id;

        public BitmapWorkerTask(int id, OnOneItemLoadedListener listener, List<Integer> idList) {
            this.id = id;
            this.listener = listener;
            this.idList = idList;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            imageUrl = params[0];
            FileDescriptor fileDescriptor = null;
            FileInputStream fileInputStream = null;
            try {
                final String key = Encrypter.encryptByMD5(imageUrl);
                DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
                if (snapShot == null) {
                    DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                    if (editor != null) {
                        OutputStream outputStream = editor.newOutputStream(0);
                        if (downloadUrlToStream(imageUrl, outputStream)) {
                            editor.commit();
                        } else {
                            editor.abort();
                        }
                    }
                    mDiskLruCache.flush();
                    snapShot = mDiskLruCache.get(key);
                }
                if (snapShot != null) {
                    fileInputStream = (FileInputStream) snapShot.getInputStream(0);
                    fileDescriptor = fileInputStream.getFD();
                }
                Bitmap bitmap = null;
                if (fileDescriptor != null) {
                    bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                }
                if (bitmap != null) {
                    putBitmapToCache(params[0], bitmap);
                }
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileDescriptor == null && fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            taskCollection.remove(this);
            mRequestImgs.remove(id);
            if (0 == mRequestIds.size() && 0 == mRequestImgs.size()) {
                listener.onLoaded(idList);
            }
        }

        private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
            HttpURLConnection urlConnection = null;
            BufferedOutputStream out = null;
            BufferedInputStream in = null;
            try {
                final URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
                out = new BufferedOutputStream(outputStream, 8 * 1024);
                int b;
                while ((b = in.read()) != -1) {
                    out.write(b);
                }
                return true;
            } catch (final IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }

}
