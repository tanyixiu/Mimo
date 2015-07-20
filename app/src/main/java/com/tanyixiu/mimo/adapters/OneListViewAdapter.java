package com.tanyixiu.mimo.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.tanyixiu.mimo.R;
import com.tanyixiu.mimo.entities.OneItemEntity;
import com.tanyixiu.mimo.entities.OneItemLoader;
import com.tanyixiu.mimo.entities.OneItemLoader.OnOneItemLoadedListener;
import com.tanyixiu.mimo.holder.OneItemViewHolder;
import com.tanyixiu.mimo.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanyixiu on 2015/7/15.
 */
public class OneListViewAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

    private Context mContext;
    private ListView mListView;
    private List<Integer> mOneItemIdList;
    private OneItemLoader mOneItemLoader;
    private boolean isAtBottom = false;
    private boolean isNextPageInLoading = false;
    private int mVisibleItemCount;
    private int mFirstVisibleIndex;

    public OneListViewAdapter(Context context, ListView listView) {
        mContext = context;
        mListView = listView;
        mOneItemIdList = new ArrayList<>();
        mListView.setOnScrollListener(this);
        mOneItemLoader = new OneItemLoader(context);
        int startOneItemId = mOneItemLoader.getMaxOneItemID();
        mListView.setOnScrollListener(OneListViewAdapter.this);
        loadNextPageOfOneItem(startOneItemId);
    }

    private void loadNextPageOfOneItem(int startOneItemId) {
        isNextPageInLoading = true;
        mOneItemLoader.loadPageOfOneItems(startOneItemId, new OnOneItemLoadedListener() {
            @Override
            public void onLoaded(List<Integer> idList) {
                isNextPageInLoading = false;
                if (null == idList || 0 == idList.size()) {
                    return;
                }
                mOneItemIdList.addAll(idList);
                notifyDataSetChanged();
            }
        });
    }

    public void cancelAllTasks() {
        mOneItemLoader.cancelAllTasks();
    }

    public void flushCache() {
        mOneItemLoader.flushCache();
    }

    @Override
    public int getCount() {
        if (null == mOneItemIdList) {
            return 0;
        }
        return mOneItemIdList.size();
    }

    @Override
    public Integer getItem(int position) {
        if (null == mOneItemIdList) {
            return null;
        }
        return mOneItemIdList.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (null == mOneItemIdList) {
            return 0;
        }
        return mOneItemIdList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final OneItemViewHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.one_item, null);
            holder = new OneItemViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (OneItemViewHolder) convertView.getTag();
        }

        int oneItemId = getItem(position);
        bindDataToHolder(holder, oneItemId);
        return convertView;
    }

    private void bindDataToHolder(OneItemViewHolder holder, int oneItemId) {
        OneItemEntity entity = mOneItemLoader.getOneItem(oneItemId);
        if (null == entity) {
            holder.clearData();
        } else {
            Bitmap bitmap = mOneItemLoader.getBitmap(entity.getImgurl());
            holder.bindData(entity, bitmap);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (SCROLL_STATE_IDLE != scrollState) {
            return;
        }
        if (isAtBottom && false == isNextPageInLoading) {
            int lastOneItemId = getItem(getCount() - 1);
            loadNextPageOfOneItem(lastOneItemId - 1);
        }

//        if (false == isNextPageInLoading) {
//            Logger.single().d("need bind data when scrolling ??????");
//            isNextPageInLoading = true;
//            int startOneItemId = getItem(mFirstVisibleIndex);
//            mOneItemLoader.loadCurrentPageOfOneItems(startOneItemId, mVisibleItemCount, new OnOneItemLoadedListener() {
//                @Override
//                public void onLoaded(List<Integer> idList) {
//                    isNextPageInLoading = false;
//                    if (null == idList || 0 == idList.size()) {
//                        Logger.single().d("do not need bind data when scrolling !");
//                        return;
//                    }
//                    Logger.single().d("need bind data when scrolling !!!!");
//                    for (int id : idList) {
//                        OneItemViewHolder holder = getViewHolder(id);
//                        if (null == holder) {
//                            continue;
//                        }
//                        Logger.single().d("bind data when scrolling :" + id);
//                        bindDataToHolder(holder, id);
//                    }
//                }
//            });
//        }
    }

    private OneItemViewHolder getViewHolder(int oneItemId) {
        return null;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (0 == totalItemCount) {
            return;
        }
        mVisibleItemCount = visibleItemCount;
        mFirstVisibleIndex = firstVisibleItem;
        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount == getCount()) {
            isAtBottom = true;
        } else {
            isAtBottom = false;
        }
    }
}