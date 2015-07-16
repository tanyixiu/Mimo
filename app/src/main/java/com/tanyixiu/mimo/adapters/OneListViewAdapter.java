package com.tanyixiu.mimo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.tanyixiu.mimo.R;
import com.tanyixiu.mimo.entities.OneItemEntity;
import com.tanyixiu.mimo.main.MainActivity;
import com.tanyixiu.mimo.performance.BitmapCache;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by tanyixiu on 2015/7/15.
 */
public class OneListViewAdapter extends BaseAdapter {

    private Context mContext;
    private ListView mListView;

    public void OneListViewAdapter(Context context, ListView listView) {
        mContext = context;
        mListView = listView;
        mListView.setOnScrollListener(mOnScrollListener);
    }

    private AbsListView.OnScrollListener mOnScrollListener =
            new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view,
                                     int firstVisibleItem,
                                     int visibleItemCount,
                                     int totalItemCount) {

                }
            };


    private List<OneItemEntity> mOneItemEntities = new LinkedList<>();

    public OneListViewAdapter(Context context, List<OneItemEntity> items) {
        mContext = context;
        if (null != items) {
            mOneItemEntities = items;
        }
    }

    public void AddItem(OneItemEntity entity) {
        if (null == entity) {
            return;
        }
        int location = getLocation(entity.getUniqueId());
        mOneItemEntities.add(location, entity);
    }

    private int getLocation(int targetId) {

        synchronized (mOneItemEntities) {
            int length = mOneItemEntities.size();
            for (int i = 0; i < length; i++) {
                int id = mOneItemEntities.get(i).getUniqueId();
                if (id < targetId) return i;
            }
            return length;
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

    class ViewHolder {

        public TextView tvNumber;
        public NetworkImageView imgShow;
        public TextView tvTitle;
        public TextView tvAuthor;
        public TextView tvDay;
        public TextView tvYeahMonth;
        public TextView tvQuote;

        public ViewHolder(View contentView) {
            this.tvNumber = (TextView) contentView.findViewById(R.id.one_tv_num);
            this.imgShow = (NetworkImageView) contentView.findViewById(R.id.one_img_show);
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

            ImageLoader imageLoader = new ImageLoader(
                    ((MainActivity) mContext).getRequestQueue(), new BitmapCache());
            this.imgShow.setImageUrl(entity.getImgurl(), imageLoader);
        }
    }
}