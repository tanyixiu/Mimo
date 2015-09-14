package com.tanyixiu.mimo.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tanyixiu.mimo.R;
import com.tanyixiu.mimo.adapters.OneItemLoader;

import java.lang.reflect.Field;
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

    @Override
    public void onResume() {
        super.onResume();
        if (null != mViewHolder && null != mViewHolder.mFindUConvenientBanner) {
            mViewHolder.mFindUConvenientBanner.startTurning(5000);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (null != mViewHolder && null != mViewHolder.mFindUConvenientBanner) {
            mViewHolder.mFindUConvenientBanner.stopTurning();
        }
    }

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

//        mViewHolder.mFindUConvenientBanner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
//            @Override
//            public LocalImageHolderView createHolder() {
//                return new LocalImageHolderView();
//            }
//        }, getLocalImages())
//                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
//                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
//                .setPageTransformer(ConvenientBanner.Transformer.DefaultTransformer);
    }

    private List<Integer> getLocalImages() {
        List<Integer> mList = new ArrayList<>();
        for (int position = 1; position < 16; position++) {
            mList.add(getResId("img_" + position, R.drawable.class));
        }
        return mList;
    }

    private int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    private List<String> getNetWorkImages() {
        List<String> mList = new ArrayList<>();
        mList.add("http://b280.photo.store.qq.com/psb?/V11uJZqM3CxP2R/HbPfqJwlGvYd8bPuUozgIctlPM8*.iCazmafKA9KQHA!/b/dGxG6abqIAAA&bo=MAPMAQAAAAAFB9o!&rf=viewer_4");
        mList.add("http://b281.photo.store.qq.com/psb?/V11uJZqM3CxP2R/Q8UJzxV.A8g1IZGCZF*1HDucWB9fHYRggt06fkBRmFs!/b/dI.9fqfPDQAA&bo=gAJ0AwAAAAAFANY!&rf=viewer_4");
        mList.add("http://b281.photo.store.qq.com/psb?/V11uJZqM3CxP2R/gBYVqBuwZhOyTDdVOAMA553dynsbwQkou*A3Kd48Iik!/b/dMSKjKeZCwAA&bo=gAJvBAAAAAAFAMo!&rf=viewer_4");
        mList.add("http://b281.photo.store.qq.com/psb?/V11uJZqM3CxP2R/3pcbht0zNnw9Fmp6G1QVxmW2CnbMFHkLEzEDwMlgrCk!/b/dOEMi6eZCwAA&bo=gAJvBAAAAAAFAMo!&rf=viewer_4");
        mList.add("http://b281.photo.store.qq.com/psb?/V11uJZqM3CxP2R/Qy3X98tjJf9zTBtlDlcLw0SXnF*mC9DxO3slkPBxVoo!/b/dOAJi6cHCwAA&bo=gAJvBAAAAAAFAMo!&rf=viewer_4");
        mList.add("http://b280.photo.store.qq.com/psb?/V11uJZqM3CxP2R/.b*XlOKG.8ulmKwMxlvoQix9jWVl386cUBPRC94e69E!/b/dJPk8KY5IAAA&bo=gAJvBAAAAAAFAMo!&rf=viewer_4");
        mList.add("http://b280.photo.store.qq.com/psb?/V11uJZqM3CxP2R/7xgwsDbJ8gici.yYCoSVJHoTBWk2QcH5dpBLOabAsus!/b/dE.956agHwAA&bo=gAJvBAAAAAAFAMo!&rf=viewer_4");
        mList.add("http://b281.photo.store.qq.com/psb?/V11uJZqM3CxP2R/E8wNkVccS8kZMO*aQ7hwKCH.r4WjiXC4.Oq3Jn9JBWg!/b/dBd5iae5CgAA&bo=gAJvBAAAAAAFAMo!&rf=viewer_4");
        mList.add("http://b280.photo.store.qq.com/psb?/V11uJZqM3CxP2R/n2D0B502lc541RS9Cj1PNTbphspHeCrzsv11WH88n7o!/b/dKS.6qa5GwAA&bo=gAJvBEAGFgsFAJE!&rf=viewer_4");
        mList.add("http://b281.photo.store.qq.com/psb?/V11uJZqM3CxP2R/Im8r07DEprROBVxAN9hXO8T8QPw4ka.pywX5X*LZUe8!/b/dLFPg6foCQAA&bo=gAJvBEAGFgsFAJE!&rf=viewer_4");
        mList.add("http://b280.photo.store.qq.com/psb?/V11uJZqM3CxP2R/AqFOBxWf8.XVIoAhpvy0saX25hN*USUslWk6lgSTDMg!/b/dL*c6qYuHwAA&bo=gAJvBEAGFgsFAJE!&rf=viewer_4");
        mList.add("http://b281.photo.store.qq.com/psb?/V11uJZqM3CxP2R/o3HnwOksBUWUamDZ6XRNY8EjdCT7JgGc4f1JFBIhKHI!/b/dCtbg6fRCgAA&bo=gAJvBEAGFgsFAJE!&rf=viewer_4");
        mList.add("http://b281.photo.store.qq.com/psb?/V11uJZqM3CxP2R/1SZeWYRICuVsIIS34GnxRV77Mc4MUZ1BctsYJe5ShlE!/b/dDHFfqeqCgAA&bo=gAJvBEAGFgsFAJE!&rf=viewer_4");
        mList.add("http://b281.photo.store.qq.com/psb?/V11uJZqM3CxP2R/AN3Y*Vji1QJ6Oy0ATCdmUdQl7**OF*h1HHoBhZOScxo!/b/dHFUg6fRCgAA&bo=gAJvBEAGFgsFAJE!&rf=viewer_4");
        mList.add("http://b280.photo.store.qq.com/psb?/V11uJZqM3CxP2R/4JwZ5YWCihyeQMhlBnZFyakCt2I.8BBXM98PnxntLkI!/b/dOXf8KZtHgAA&bo=gAJvBEAGFgsFAJE!&rf=viewer_4");
        mList.add("http://b280.photo.store.qq.com/psb?/V11uJZqM3CxP2R/isddBjsiPY1vfHZB*pZHyib4SnBlCnkoefo8JvYTrjA!/b/dPVq76YmHQAA&bo=gAJvBEAGFgsFAJE!&rf=viewer_4");
        return mList;
    }

    class NetworkImageHolderView implements CBPageAdapter.Holder<String> {

        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, String data) {
            ImageLoader.getInstance().displayImage(data, imageView);
        }
    }

    class LocalImageHolderView implements CBPageAdapter.Holder<Integer> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, Integer data) {
            imageView.setImageResource(data);
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
