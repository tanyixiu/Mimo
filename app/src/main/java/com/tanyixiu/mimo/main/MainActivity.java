package com.tanyixiu.mimo.main;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.tanyixiu.mimo.R;
import com.tanyixiu.mimo.activities.BaseActivity;
import com.tanyixiu.widgets.slidedrawer.SlideDrawer;


public class MainActivity extends BaseActivity {

    private SlideDrawer mSlideDrawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        setContentView(rootView);
        setUpMenu();
    }

    private void setUpMenu() {
        mSlideDrawer = new SlideDrawer(this);
        mSlideDrawer.setBackground(R.drawable.menu_background);
        mSlideDrawer.attachToActivity(this);

        View menuView = LayoutInflater.from(this).inflate(R.layout.activity_main_drawermenu, null);
        mSlideDrawer.setMenuView(menuView);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mSlideDrawer.dispatchTouchEvent(ev);
    }

    private void changeFragment(Fragment targetFragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

}
