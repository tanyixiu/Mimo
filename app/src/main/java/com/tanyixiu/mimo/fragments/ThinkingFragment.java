package com.tanyixiu.mimo.fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.melnykov.fab.FloatingActionButton;
import com.tanyixiu.MimoApp;
import com.tanyixiu.mimo.R;
import com.tanyixiu.mimo.adapters.ThinkingItemLoader;
import com.tanyixiu.mimo.moduls.ThinkingItem;
import com.tanyixiu.mimo.utils.ToolUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ThinkingFragment extends Fragment {

    private View mRootView;
    private ViewHolder mViewHolder;
    private ThinkingItemLoader mThinkingItemLoader;
    private ThinkingListAdapter mThinkingListAdapter;

    public static ThinkingFragment getNewInstance() {
        return new ThinkingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != mRootView) {
            return mRootView;
        }
        mRootView = inflater.inflate(R.layout.fragment_thinking, container, false);
        initView();
        return mRootView;
    }

    private void initView() {
        mViewHolder = new ViewHolder(mRootView);
        mViewHolder.mThinkBtnAdd.attachToListView(mViewHolder.mThinkListview);
        mThinkingItemLoader = new ThinkingItemLoader();

        mViewHolder.mThinkBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAddClick();
            }
        });

        mThinkingItemLoader.loadThinkItems(new ThinkingItemLoader.OnThinkingItemLoadedListener() {
            @Override
            public void onLoaded(List<ThinkingItem> thinkingItems) {
                mThinkingListAdapter = new ThinkingListAdapter(getActivity(), mViewHolder.mThinkListview, thinkingItems);
                mViewHolder.mThinkListview.setAdapter(mThinkingListAdapter);
            }
        });
    }

    private void doAddClick() {
        Context context = getActivity();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_thinking, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.show();

        final EditText etIdea = (EditText) view.findViewById(R.id.dialogthing_ed_idea);
        Button btnSave = (Button) view.findViewById(R.id.dialogthing_btn_save);
        Button btnCancel = (Button) view.findViewById(R.id.dialogthing_btn_cancel);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idea = String.valueOf(etIdea.getText());
                if (TextUtils.isEmpty(idea)) {
                    return;
                }
                alertDialog.dismiss();
                ThinkingItem item = ThinkingItem.createItem(idea);
                if (null == item) {
                    return;
                }
                item.save();
                mThinkingListAdapter.addItem(item);
                mThinkingListAdapter.notifyDataSetChanged();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


    class ThinkingListAdapter extends BaseAdapter {

        private Context mContext;
        private SwipeMenuListView mListView;
        private List<ThinkingItem> mItems;

        public ThinkingListAdapter(Context context, SwipeMenuListView listView, List<ThinkingItem> items) {
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
                ThinkingItem item = (ThinkingItem) getItem(position);
                btnDeleteClick(item);
                return true;
            }
        };

        private void btnDeleteClick(ThinkingItem item) {
            if (null == item) {
                return;
            }
            item.delete();
            mItems.remove(item);
            notifyDataSetChanged();
        }

        public synchronized void addItem(ThinkingItem item) {
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
            ThinkingItemViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.thinking_list_item, null);
                holder = new ThinkingItemViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ThinkingItemViewHolder) convertView.getTag();
            }
            holder.bindData((ThinkingItem) getItem(position));
            return convertView;
        }


        class ThinkingItemViewHolder {

            private ThinkingItem mThinkingItem;

            @InjectView(R.id.thinklistitem_img_mark)
            ImageView mThinklistitemImgMark;
            @InjectView(R.id.thinklistitem_tv_idea)
            TextView mThinklistitemTvIdea;

            ThinkingItemViewHolder(View view) {
                ButterKnife.inject(this, view);
                mThinklistitemImgMark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnMarkClick(mThinkingItem);
                    }
                });
            }

            private void btnMarkClick(ThinkingItem item) {
                if (null == item) {
                    return;
                }
                item.setIsMark(!item.getIsMark());
                item.save();
                notifyDataSetChanged();
            }

            public void bindData(ThinkingItem item) {
                if (null == item) {
                    return;
                }
                mThinkingItem = item;

                mThinklistitemTvIdea.setText(item.getIdea());
                int resId = item.getIsMark() ? R.drawable.ic_star_fill : R.drawable.ic_star_border;
                mThinklistitemImgMark.setImageResource(resId);
            }
        }
    }

    protected static class ViewHolder {
        @InjectView(R.id.think_listview)
        SwipeMenuListView mThinkListview;
        @InjectView(R.id.think_btn_add)
        FloatingActionButton mThinkBtnAdd;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}