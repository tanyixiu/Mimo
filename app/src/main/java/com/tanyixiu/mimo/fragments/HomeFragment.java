package com.tanyixiu.mimo.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tanyixiu.mimo.R;
import com.tanyixiu.mimo.adapters.OneListViewAdapter;

/**
 * Created by tanyixiu on 2015/7/11.
 */
public class HomeFragment extends Fragment {
    private ListView oneListView;
    private OneListViewAdapter mOneListViewAdapter;

    public static HomeFragment getNewInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        oneListView = (ListView) inflater.inflate(R.layout.fragment_home, container, false);
        mOneListViewAdapter = new OneListViewAdapter(getActivity(), oneListView);
        oneListView.setAdapter(mOneListViewAdapter);
        return oneListView;
    }

    @Override
    public void onPause() {
        super.onPause();
        mOneListViewAdapter.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOneListViewAdapter.cancelAllTasks();
    }
}