package com.tanyixiu.mimo.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tanyixiu.mimo.R;
import com.tanyixiu.mimo.entities.OneItemEntity;
import com.tanyixiu.mimo.main.MainActivity;
import com.tanyixiu.mimo.performance.BitmapCache;
import com.tanyixiu.mimo.utils.Logger;
import com.tanyixiu.mimo.utils.OneItemParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tanyixiu on 2015/7/15.
 */
public class OneListViewAdapter extends BaseAdapter {

    private Context mContext;
    private ListView mListView;
    private BitmapCache mBitmapMemoryCache;
    private List<OneItemEntity> mOneItemEntities;
    private Set<BitmapWorkerTask> mTaskCollection;

    private int mFirstVisibleItem;
    private int mVisibleItemCount;
    private boolean isFirstEnter = true;

    public OneListViewAdapter(Context context, ListView listView) {
        mContext = context;
        mListView = listView;
        mBitmapMemoryCache = new BitmapCache();
        mOneItemEntities = new ArrayList<>();
        mTaskCollection = new HashSet<>();
        mListView.setOnScrollListener(mOnScrollListener);
        loadFirstOneItem();
    }

    private AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

            Logger.getInstance().d("onScrollStateChanged:scrollState=" + scrollState);
            if (scrollState == SCROLL_STATE_IDLE) {
                loadBitmaps(mFirstVisibleItem, mVisibleItemCount);
            } else {
                cancelAllTasks();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            mFirstVisibleItem = firstVisibleItem;
            mVisibleItemCount = visibleItemCount;

            Logger.getInstance().d("onScrollStateChanged:totalItemCount=" + totalItemCount);
//            loadFirstOneItem();
//            if (isFirstEnter && visibleItemCount > 0) {
//                loadBitmaps(firstVisibleItem, visibleItemCount);
//                isFirstEnter = false;
//            }
        }
    };

    private void loadFirstOneItem() {
        int oneId = OneItemEntity.getIdByDate(new Date());
        if (0 != mOneItemEntities.size() && oneId == mOneItemEntities.get(0).getId()) {
            return;
        }
        String oneUrl = OneItemEntity.getOneUrlById(oneId);
        requestOneItem(oneUrl);
    }

    private Response.Listener<String> mListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            OneItemEntity entity = null;
            try {
                entity = OneItemParser.parser(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (null == entity) {
                return;
            }
            AddItem(entity);
            notifyDataSetChanged();
        }
    };

    private Response.ErrorListener mErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Logger.getInstance().e("OneListViewAdapter:" + volleyError.getMessage());
        }
    };

    private void requestOneItem(String oneUrl) {
        StringRequest request = new StringRequest(oneUrl, mListener, mErrorListener);
        ((MainActivity) mContext).getRequestQueue().add(request);
    }


    private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
        try {
            for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {

                String imageUrl = getItem(i).getImgurl();
                Bitmap bitmap = mBitmapMemoryCache.getBitmap(imageUrl);
                if (bitmap == null) {
                    BitmapWorkerTask task = new BitmapWorkerTask();
                    mTaskCollection.add(task);
                    task.execute(imageUrl);
                } else {
                    ImageView imageView = (ImageView) mListView.findViewWithTag(imageUrl);
                    if (imageView != null && bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelAllTasks() {
        if (mTaskCollection != null) {
            for (BitmapWorkerTask task : mTaskCollection) {
                task.cancel(false);
            }
        }
    }

    public void AddItem(OneItemEntity entity) {
        if (null == entity) {
            return;
        }
        synchronized (mOneItemEntities) {
            int length = mOneItemEntities.size();
            for (int i = 0; i < length; i++) {
                int id = mOneItemEntities.get(i).getId();

                if (id == entity.getId()) return;

                if (id < entity.getId()) {
                    mOneItemEntities.add(i, entity);
                    return;
                }
            }
            mOneItemEntities.add(length, entity);
        }
    }

    @Override
    public int getCount() {
        if (null == mOneItemEntities) {
            return 0;
        }
        return mOneItemEntities.size();
    }

    @Override
    public OneItemEntity getItem(int position) {
        if (null == mOneItemEntities) {
            return null;
        }
        return mOneItemEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Logger.getInstance().d("getView:position=" + position);
        ViewHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.one_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OneItemEntity entity = getItem(position);
        holder.bindData(entity);
        return convertView;
    }

    private void setImageView(String imageUrl, ImageView imageView) {
        Bitmap bitmap = mBitmapMemoryCache.getBitmap(imageUrl);
        if (null != bitmap) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.empty_photo);
        }
    }

    class ViewHolder {

        public TextView tvNumber;
        public ImageView imgShow;
        public TextView tvTitle;
        public TextView tvAuthor;
        public TextView tvDay;
        public TextView tvYeahMonth;
        public TextView tvQuote;

        public ViewHolder(View contentView) {
            this.tvNumber = (TextView) contentView.findViewById(R.id.one_tv_num);
            this.imgShow = (ImageView) contentView.findViewById(R.id.one_img_show);
            this.tvTitle = (TextView) contentView.findViewById(R.id.one_tv_title);
            this.tvAuthor = (TextView) contentView.findViewById(R.id.one_tv_author);
            this.tvDay = (TextView) contentView.findViewById(R.id.one_tv_day);
            this.tvYeahMonth = (TextView) contentView.findViewById(R.id.one_tv_monthyear);
            this.tvQuote = (TextView) contentView.findViewById(R.id.one_tv_quote);
        }

        public void bindData(OneItemEntity entity) {
            this.tvNumber.setText(entity.getNumber());
            this.tvTitle.setText(entity.getTitle());
            this.tvAuthor.setText(entity.getAuthor());
            this.tvDay.setText(entity.getDay());
            this.tvYeahMonth.setText(entity.getMonth() + "," + entity.getYear());
            this.tvQuote.setText(entity.getQuote());
            this.imgShow.setTag(entity.getImgurl());
            setImageView(entity.getImgurl(), this.imgShow);
        }
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

        private String imgUrl;

        @Override
        protected Bitmap doInBackground(String... params) {
            imgUrl = params[0];
            Bitmap bitmap = downloadBitmap(imgUrl);
            if (null != bitmap) {
                mBitmapMemoryCache.putBitmap(imgUrl, bitmap);
            }
            return bitmap;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = (ImageView) mListView.findViewWithTag(imgUrl);
            if (null != imageView && null != bitmap) {
                imageView.setImageBitmap(bitmap);
            }

        }

        private Bitmap downloadBitmap(String imgUrl) {
            Bitmap bitmap = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(imgUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(5 * 1000);
                connection.setReadTimeout(10 * 1000);
                bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != connection) {
                    connection.disconnect();
                }
            }
            return bitmap;
        }
    }
}