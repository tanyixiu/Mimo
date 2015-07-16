package com.tanyixiu.mimo.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.tanyixiu.mimo.R;
import com.tanyixiu.mimo.adapters.OneListViewAdapter;
import com.tanyixiu.mimo.entities.OneItemEntity;
import com.tanyixiu.mimo.main.MainActivity;
import com.tanyixiu.mimo.performance.BitmapCache;
import com.tanyixiu.mimo.utils.OneItemParser;
import com.tanyixiu.test.Test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

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

}