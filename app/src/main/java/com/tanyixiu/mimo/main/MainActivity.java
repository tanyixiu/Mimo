package com.tanyixiu.mimo.main;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.tanyixiu.mimo.R;
import com.tanyixiu.mimo.activities.BaseActivity;
import com.tanyixiu.mimo.fragments.FindUFragment;
import com.tanyixiu.mimo.fragments.HomeFragment;
import com.tanyixiu.mimo.fragments.RunFragment;
import com.tanyixiu.mimo.fragments.ToysFragment;
import com.tanyixiu.widgets.slidedrawer.SlideDrawer;


public class MainActivity extends BaseActivity {

    private SlideDrawer mSlideDrawer;
    private RelativeLayout main_menu_toggle;
    private TabHolder mTabHolder;
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        mRequestQueue = Volley.newRequestQueue(this);
        setContentView(rootView);
        initView(rootView);
        setUpMenu();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mSlideDrawer.dispatchTouchEvent(ev);
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    private TabHolderOnClickListener mTabHolderOnClickListener =
            new TabHolderOnClickListener() {
                @Override
                public void onTabSelected(Fragment fragment) {
                    changeFragment(fragment);
                }
            };


    private void initView(View rootView) {
        mTabHolder = new TabHolder(rootView);
        mTabHolder.setTabHolderOnClickListener(mTabHolderOnClickListener);
        mTabHolder.setDefaultTabSelected();
    }

    private void setUpMenu() {
        mSlideDrawer = new SlideDrawer(this);
        mSlideDrawer.setBackground(R.drawable.menu_background);
        mSlideDrawer.attachToActivity(this);

        View menuView = LayoutInflater.from(this).inflate(R.layout.activity_main_drawermenu, null);
        mSlideDrawer.setMenuView(menuView);
    }

    private void changeFragment(Fragment targetFragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment)
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    interface TabHolderOnClickListener {
        void onTabSelected(Fragment fragment);
    }

    class TabHolder {

        private TextView tabHome;
        private TextView tabFindU;
        private TextView tabToys;
        private TextView tabRun;
        private TabHolderOnClickListener mTabHolderOnClickListener;

        public TabHolder(View rootView) {
            tabHome = (TextView) rootView.findViewById(R.id.main_tab_home);
            tabFindU = (TextView) rootView.findViewById(R.id.main_tab_findu);
            tabToys = (TextView) rootView.findViewById(R.id.main_tab_toys);
            tabRun = (TextView) rootView.findViewById(R.id.main_tab_run);

            tabHome.setOnClickListener(mOnClickListener);
            tabFindU.setOnClickListener(mOnClickListener);
            tabToys.setOnClickListener(mOnClickListener);
            tabRun.setOnClickListener(mOnClickListener);
        }

        public void setTabHolderOnClickListener(
                TabHolderOnClickListener tabHolderOnClickListener) {
            mTabHolderOnClickListener = tabHolderOnClickListener;
        }

        public void setDefaultTabSelected() {
            setTabSelected(tabHome);
        }

        private View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelected(v);
            }
        };

        private void resetBackground() {
            tabHome.setBackgroundResource(R.color.bkg_color_green_light);
            tabFindU.setBackgroundResource(R.color.bkg_color_green_light);
            tabToys.setBackgroundResource(R.color.bkg_color_green_light);
            tabRun.setBackgroundResource(R.color.bkg_color_green_light);
        }

        private void setTabSelected(View tab) {
            resetBackground();
            tab.setBackgroundResource(R.color.bkg_color_green_dark);
            if (null == mTabHolderOnClickListener) {
                return;
            }

            Fragment selectedFragment = getSelectedFragment(tab);
            mTabHolderOnClickListener.onTabSelected(selectedFragment);
        }

        private Fragment getSelectedFragment(View tab) {
            Object tag = tab.getTag();
            if (null != tag) {
                return (Fragment) tab.getTag();
            }
            Fragment fragment = null;
            switch (tab.getId()) {
                case R.id.main_tab_home:
                    fragment = HomeFragment.getNewInstance();
                    break;
                case R.id.main_tab_findu:
                    fragment = FindUFragment.getNewInstance();
                    break;
                case R.id.main_tab_toys:
                    fragment = ToysFragment.getNewInstance();
                    break;
                case R.id.main_tab_run:
                    fragment = RunFragment.getNewInstance();
                    break;
                default:
                    break;
            }
            tab.setTag(fragment);
            return fragment;
        }
    }
}
