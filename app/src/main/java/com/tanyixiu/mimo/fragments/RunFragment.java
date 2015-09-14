package com.tanyixiu.mimo.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tanyixiu.mimo.R;
import com.tanyixiu.mimo.adapters.OneItemLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class RunFragment extends Fragment {

    private View mRootView;
    private ViewHolder mViewHolder;

    public static RunFragment getNewInstance() {
        return new RunFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != mRootView) {
            return mRootView;
        }
        mRootView = inflater.inflate(R.layout.fragment_run, container, false);
        initView();
        return mRootView;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (null != mViewHolder && null != mViewHolder.mFindUConvenientBanner) {
//            mViewHolder.mFindUConvenientBanner.startTurning(5000);
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (null != mViewHolder && null != mViewHolder.mFindUConvenientBanner) {
//            mViewHolder.mFindUConvenientBanner.stopTurning();
//        }
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void initView() {
        mViewHolder = new ViewHolder(mRootView);
        mViewHolder.mFindUConvenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, getNetWorkImages())
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setPageTransformer(ConvenientBanner.Transformer.DefaultTransformer);

    }

    private List<String> getNetWorkImages() {
        List<String> mList = new ArrayList<>();
        int number = OneItemLoader.getMaxOneItemID();
        for (int i = number; i > number - 7 && i > 0; i--) {
            mList.add(OneItemLoader.getOneImageUrlById(i));
        }
        return mList;
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

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'fragment_find_u.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    protected static class ViewHolder {
        @InjectView(R.id.run_convenientBanner)
        ConvenientBanner mFindUConvenientBanner;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
