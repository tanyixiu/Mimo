package com.tanyixiu.mimo.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanyixiu.mimo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RunFragment extends Fragment {

    public static RunFragment getNewInstance() {
        return new RunFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_run, container, false);
    }
}
