package com.tanyixiu.mimo.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanyixiu.mimo.R;

public class ThinkingFragment extends Fragment {

    private View mRootView;

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
        return mRootView;
    }
}