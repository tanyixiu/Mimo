package com.tanyixiu.mimo.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanyixiu.mimo.R;

public class FindUFragment extends Fragment {


    public static FindUFragment getNewInstance() {
        return new FindUFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find_u, container, false);
    }
}
