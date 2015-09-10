package com.tanyixiu.mimo.fragments;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.melnykov.fab.FloatingActionButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tanyixiu.mimo.R;
import com.tanyixiu.mimo.activities.MainActivity;
import com.tanyixiu.mimo.adapters.OneItemLoader;
import com.tanyixiu.mimo.adapters.ThinkingItemLoader;
import com.tanyixiu.mimo.moduls.ThinkingItem;
import com.yalantis.phoenix.PullToRefreshView;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public void onResume() {
        super.onResume();
        if (null != mViewHolder && null != mViewHolder.mConvenientBanner) {
            mViewHolder.mConvenientBanner.startTurning(5000);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != mViewHolder && null != mViewHolder.mConvenientBanner) {
            mViewHolder.mConvenientBanner.stopTurning();
        }
    }

    private void initView() {
        mViewHolder = new ViewHolder(mRootView);
        mThinkingItemLoader = new ThinkingItemLoader();

        mViewHolder.mConvenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, getNetWorkImages())
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setPageTransformer(ConvenientBanner.Transformer.DefaultTransformer);

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

    private List<String> getNetWorkImages() {
        List<String> mList = new ArrayList<>();
        int number = OneItemLoader.getMaxOneItemID();
        for (int i = number; i > number - 7 && i > 0; i--) {
            mList.add(OneItemLoader.getOneImageUrlById(i));
        }
        return mList;
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
        private ListView mListView;
        private List<ThinkingItem> mItems;

        public ThinkingListAdapter(Context context, ListView listView, List<ThinkingItem> items) {
            mItems = items;
            mContext = context;
            mListView = listView;
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

    }

    class NetworkImageHolderView implements CBPageAdapter.Holder<String> {

        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, String data) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.getInstance().displayImage(data, imageView);
        }
    }

    protected static class ThinkingItemViewHolder {
        @InjectView(R.id.thinklistitem_tv_idea)
        TextView mThinklistitemTvIdea;

        ThinkingItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

        public void bindData(ThinkingItem item) {
            if (null == item) {
                return;
            }
            mThinklistitemTvIdea.setText(item.getIdea());
        }
    }

    protected static class ViewHolder {
        @InjectView(R.id.think_listview)
        ListView mThinkListview;
        @InjectView(R.id.think_btn_add)
        FloatingActionButton mThinkBtnAdd;
        @InjectView(R.id.think_convenientBanner)
        ConvenientBanner mConvenientBanner;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}