package com.tanyixiu.mimo.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanyixiu.mimo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindUFragment extends Fragment {


    public static FindUFragment getNewInstance() {
        return new FindUFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_u, container, false);
    }


}
