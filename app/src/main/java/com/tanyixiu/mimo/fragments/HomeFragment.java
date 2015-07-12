package com.tanyixiu.mimo.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tanyixiu.mimo.R;

/**
 * Created by tanyixiu on 2015/7/11.
 */
public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_home, container, false);
        setUpViews(parentView);
        return parentView;
    }

    private void setUpViews(View rootView) {

        rootView.findViewById(R.id.btn_open_menu)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Hi", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
