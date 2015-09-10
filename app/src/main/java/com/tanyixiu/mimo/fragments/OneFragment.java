package com.tanyixiu.mimo.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.tanyixiu.mimo.R;
import com.tanyixiu.mimo.adapters.OneItemLoader;
import com.tanyixiu.mimo.adapters.OneItemLoader.OnOneItemLoadedListener;
import com.tanyixiu.mimo.moduls.OneItem;
import com.tanyixiu.mimo.view.OneView;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Mimo on 2015/9/9.
 */
public class OneFragment extends Fragment {

    private View mRootView;
    private ViewHolder mViewHolder;
    private OneItemLoader mOneItemLoader;

    public static OneFragment getNewInstance() {
        return new OneFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != mRootView) {
            return mRootView;
        }
        mRootView = inflater.inflate(R.layout.fragment_one, container, false);
        initView();
        return mRootView;
    }


    private void initView() {
        mOneItemLoader = new OneItemLoader();
        mViewHolder = new ViewHolder(mRootView);
        mViewHolder.toggleLoading(true);
        int id = OneItemLoader.getMaxOneItemID() - 1;
        mOneItemLoader.loadOneItem(id, new OnOneItemLoadedListener() {
            @Override
            public void onLoaded(OneItem oneItem) {
                OneListAdapter mOneListAdapter = new OneListAdapter(getActivity(), oneItem, mViewHolder.mOneListview, mViewHolder.mOneRefreshview);
                mViewHolder.mOneListview.setAdapter(mOneListAdapter);
                mViewHolder.toggleLoading(false);
            }
        });
    }


    private class OneListAdapter extends BaseAdapter {

        private Context mContext;
        private ListView mListView;
        private PullToRefreshView mRefreshView;
        private boolean isAtBottom = false;
        private boolean isLoading = false;

        private List<OneItem> mOneItems = new ArrayList<>();

        public OneListAdapter(Context context, OneItem oneItem, ListView listView, PullToRefreshView refreshView) {
            mContext = context;
            mListView = listView;
            mRefreshView = refreshView;
            appendItem(oneItem);
            mRefreshView.setOnRefreshListener(mOnRefreshListener);
            mListView.setOnScrollListener(mOnScrollListener);
        }

        private PullToRefreshView.OnRefreshListener mOnRefreshListener
                = new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isLoading) {
                            return;
                        }
                        loadFirstItem();
                    }
                }, 800);
            }
        };

        private AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (SCROLL_STATE_IDLE != scrollState) {
                    return;
                }
                if (!isAtBottom) {
                    return;
                }
                if (isLoading) {
                    return;
                }
                loadNextItem();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (0 == totalItemCount) {
                    return;
                }
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount == getCount()) {
                    isAtBottom = true;
                } else {
                    isAtBottom = false;
                }
            }
        };

        private void loadFirstItem() {
            int firstId = OneItemLoader.getMaxOneItemID();
            int curFirstId = getFirstItem().getId();
            if (firstId == curFirstId) {
                mRefreshView.setRefreshing(false);
                return;
            }
            isLoading = true;
            mOneItemLoader.loadOneItem(curFirstId + 1, new OnOneItemLoadedListener() {
                @Override
                public void onLoaded(OneItem oneItem) {
                    if (null != oneItem) {
                        addFirstItem(oneItem);
                        notifyDataSetChanged();
                    }
                    mRefreshView.setRefreshing(false);
                    isLoading = false;
                }
            });
        }

        private void loadNextItem() {
            int curMinId = getEndItem().getId();
            if (1 == curMinId) {
                return;
            }
            isLoading = true;
            mOneItemLoader.loadOneItem(curMinId - 1, new OnOneItemLoadedListener() {
                @Override
                public void onLoaded(OneItem oneItem) {
                    if (null != oneItem) {
                        appendItem(oneItem);
                        notifyDataSetChanged();
                    }
                    isLoading = false;
                }
            });
        }

        public void appendItem(OneItem oneItem) {
            if (null == oneItem) {
                return;
            }
            synchronized (mOneItems) {
                mOneItems.add(oneItem);
            }
        }

        public void addFirstItem(OneItem oneItem) {
            if (null == oneItem) {
                return;
            }
            synchronized (mOneItems) {
                mOneItems.add(0, oneItem);
            }
        }

        public OneItem getFirstItem() {
            synchronized (mOneItems) {
                return mOneItems.get(0);
            }
        }

        public OneItem getEndItem() {
            synchronized (mOneItems) {
                int size = mOneItems.size();
                if (0 == size) {
                    return null;
                }
                return mOneItems.get(size - 1);
            }
        }

        @Override
        public int getCount() {
            if (null == mOneItems) {
                return 0;
            }
            return mOneItems.size();
        }

        @Override
        public OneItem getItem(int position) {
            if (null == mOneItems) {
                return null;
            }
            return mOneItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mOneItems.get(position).getId();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            OneItem oneItem = getItem(position);
            OneView oneView;
            if (null == convertView) {
                oneView = new OneView(mContext);
            } else {
                oneView = (OneView) convertView;
            }

            if (oneView.getTagId() == oneItem.getId()) {
                return oneView;
            }

            oneView.setTagId(oneItem.getId());
            oneView.bindData(oneItem);
            return oneView;
        }
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'fragment_one.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    protected static class ViewHolder {
        @InjectView(R.id.one_prgbar)
        ProgressBar mProgressBar;
        @InjectView(R.id.one_listview)
        ListView mOneListview;
        @InjectView(R.id.one_refreshview)
        PullToRefreshView mOneRefreshview;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

        public void toggleLoading(boolean isLoading) {
            mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            mOneListview.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        }
    }
}