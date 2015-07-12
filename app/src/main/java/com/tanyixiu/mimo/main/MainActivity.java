package com.tanyixiu.mimo.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tanyixiu.mimo.R;
import com.tanyixiu.mimo.main.fragments.HomeFragment;
import com.tanyixiu.widgets.residemenu.ResideMenu;
import com.tanyixiu.widgets.residemenu.ResideMenuItem;


public class MainActivity extends Activity {

    private ResideMenu resideMenu;
    private MainActivity mContext;
    private ResideMenuItem itemHome;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintColor(getResources().getColor(android.R.color.holo_green_dark));
            tintManager.setStatusBarTintResource(R.drawable.menu_background);
        }

        mContext = this;
        setUpMenu();
        if (savedInstanceState == null) {
            changeFragment(new HomeFragment());
        }
    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome = new ResideMenuItem(this, R.drawable.icon_home, "Home");

        itemHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new HomeFragment());
                resideMenu.closeMenu();
            }
        });

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
        }

        @Override
        public void closeMenu() {
        }
    };

    private void changeFragment(Fragment targetFragment) {
        resideMenu.clearIgnoredViewList();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu() {
        return resideMenu;
    }
}
