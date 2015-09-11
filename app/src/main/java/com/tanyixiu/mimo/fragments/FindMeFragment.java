package com.tanyixiu.mimo.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.melnykov.fab.FloatingActionButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tanyixiu.MimoApp;
import com.tanyixiu.mimo.R;
import com.tanyixiu.mimo.activities.BookEditActivity;
import com.tanyixiu.mimo.adapters.BookItemLoader;
import com.tanyixiu.mimo.moduls.BookItem;
import com.tanyixiu.mimo.utils.ToolUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FindMeFragment extends Fragment {

    private View mRootView;
    private ViewHolder mViewHolder;
    private BookItemLoader mBookItemLoader;
    private FindMeListAdapter mFindMeListAdapter;

    public static FindMeFragment getNewInstance() {
        return new FindMeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != mRootView) {
            return mRootView;
        }
        mRootView = inflater.inflate(R.layout.fragment_find_me, container, false);
        initView();
        return mRootView;
    }

    private void initView() {
        mViewHolder = new ViewHolder(mRootView);
        mBookItemLoader = new BookItemLoader();
        mBookItemLoader.loadBookItems(new BookItemLoader.OnBookItemLoadedListener() {
            @Override
            public void onLoaded(List<BookItem> bookItems) {
                mFindMeListAdapter = new FindMeListAdapter(getActivity(), mViewHolder.mFindmeListview, bookItems);
                mViewHolder.mFindmeListview.setAdapter(mFindMeListAdapter);
            }
        });
        mViewHolder.mFinemeBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddClick();
            }
        });
    }

    private void btnAddClick() {
        Intent intent = new Intent(MimoApp.getContext(), BookEditActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (1 != requestCode) {
            return;
        }
        if (null == data) {
            return;
        }
        BookItem item = data.getParcelableExtra("bookitem");
        if (null == item) {
            return;
        }
        mFindMeListAdapter.addItem(item);
        mFindMeListAdapter.notifyDataSetChanged();
    }

    class FindMeListAdapter extends BaseAdapter {

        private Context mContext;
        private SwipeMenuListView mListView;
        private List<BookItem> mItems;

        public FindMeListAdapter(Context context, SwipeMenuListView listView, List<BookItem> items) {
            mItems = items;
            mContext = context;
            mListView = listView;
            mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
            mListView.setMenuCreator(mMenuCreator);
            mListView.setOnMenuItemClickListener(mOnMenuItemClickListener);
        }

        private SwipeMenuCreator mMenuCreator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu swipeMenu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(MimoApp.getContext());
                deleteItem.setBackground(R.color.bkg_color_orange);
                deleteItem.setWidth(ToolUtils.dp2px(90));
                deleteItem.setIcon(R.drawable.ic_delete);
                swipeMenu.addMenuItem(deleteItem);
            }
        };

        private SwipeMenuListView.OnMenuItemClickListener mOnMenuItemClickListener = new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu swipeMenu, int index) {
                BookItem item = (BookItem) getItem(position);
                if (0 == index) {
                    btnDeleteClick(item);
                }
                return true;
            }
        };

        private void btnDeleteClick(final BookItem item) {
            if (null == item) {
                return;
            }
            mBookItemLoader.deleteBookItem(item, new BookItemLoader.OnBookItemDeleteListener() {
                @Override
                public void onDeleted(boolean success) {
                    if (!success) {
                        return;
                    }
                    mItems.remove(item);
                    notifyDataSetChanged();
                }
            });
        }

        public synchronized void addItem(BookItem item) {
            if (null == item) {
                return;
            }
            if (null == mItems) {
                mItems = new ArrayList<>();
            }
            mItems.add(0, item);
        }

        @Override
        public int getCount() {
            if (null == mItems) {
                return 0;
            }
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            if (null == mItems || 0 == mItems.size()) {
                return null;
            }
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FindMeItemViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.findme_list_item, null);
                holder = new FindMeItemViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (FindMeItemViewHolder) convertView.getTag();
            }
            holder.bindData((BookItem) getItem(position));
            return convertView;
        }


        class FindMeItemViewHolder {

            private BookItem mBookItem;

            @InjectView(R.id.findme_tv_name)
            TextView mFindmeTvName;
            @InjectView(R.id.findme_tv_digest)
            TextView mFindmeTvDigest;
            @InjectView(R.id.findme_tv_author)
            TextView mFindmeTvAuthor;
            @InjectView(R.id.findme_tv_state)
            TextView mFindMeTvState;
            @InjectView(R.id.findme_img_cover)
            ImageView mFindMeImgCover;

            FindMeItemViewHolder(View view) {
                ButterKnife.inject(this, view);
            }

            public void bindData(BookItem item) {
                if (null == item) {
                    return;
                }
                mBookItem = item;
                mFindmeTvName.setText(item.getName());
                mFindmeTvAuthor.setText(item.getAuthor());
                mFindmeTvDigest.setText(item.getDigest());
                mFindMeTvState.setText(item.getStateName());
                mFindMeTvState.setBackgroundResource(item.getStateColor());
                ImageLoader.getInstance().displayImage(item.getCoverUrl(), this.mFindMeImgCover);
            }
        }
    }

    protected static class ViewHolder {
        @InjectView(R.id.findme_listview)
        SwipeMenuListView mFindmeListview;
        @InjectView(R.id.fineme_btn_add)
        FloatingActionButton mFinemeBtnAdd;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}