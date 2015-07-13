package com.tanyixiu.mimo.fragments;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.tanyixiu.mimo.R;

/**
 * Created by tanyixiu on 2015/7/11.
 */
public class HomeFragment extends Fragment {

    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_home, container, false);
        setUpViews(parentView);
        return parentView;
    }

    private void setUpViews(View rootView) {
        textView = (TextView) rootView.findViewById(R.id.btn_open_menu);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnimator();
            }
        });
    }

    private void showAnimator() {
        textView.setPivotX(0);
        textView.setPivotY(0);
        ObjectAnimator animator = ObjectAnimator.ofFloat(textView, "rotationY", 0.0f, 90.0f,0.0f);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(2500);
        animator.start();
    }

}
