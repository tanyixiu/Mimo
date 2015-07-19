package com.tanyixiu.mimo.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;

import com.tanyixiu.mimo.R;
import com.tanyixiu.mimo.adapters.PhotoWallAdapter;
import com.tanyixiu.test.Test;

public class ToysFragment extends Fragment {

    private GridView mPhotoWall;
    private PhotoWallAdapter mAdapter;

    private static final int mImageThumbSize = 100;
    private static final int mImageThumbSpacing = 1;

    public static ToysFragment getNewInstance() {
        return new ToysFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPhotoWall = (GridView) inflater.inflate(R.layout.fragment_toys, container, false);

//        mAdapter = new PhotoWallAdapter(this.getActivity(), 0, Test.imageThumbUrls, mPhotoWall);
//        mPhotoWall.setAdapter(mAdapter);
//        mPhotoWall.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//
//                    @Override
//                    public void onGlobalLayout() {
//                        final int numColumns = (int) Math.floor(mPhotoWall
//                                .getWidth()
//                                / (mImageThumbSize + mImageThumbSpacing));
//                        if (numColumns > 0) {
//                            int columnWidth = (mPhotoWall.getWidth() / numColumns)
//                                    - mImageThumbSpacing;
//                            mAdapter.setItemHeight(columnWidth);
//                            mPhotoWall.getViewTreeObserver()
//                                    .removeGlobalOnLayoutListener(this);
//                        }
//                    }
//                });

        return mPhotoWall;
    }


}
