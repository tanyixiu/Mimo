package com.tanyixiu.mimo.main.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanyixiu.mimo.R;
import com.tanyixiu.mimo.main.MainActivity;
import com.tanyixiu.widgets.residemenu.ResideMenu;

/**
 * Created by tanyixiu on 2015/7/11.
 */
public class HomeFragment extends Fragment {

    private ResideMenu resideMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_home, container, false);
        setUpViews(parentView);
        return parentView;
    }

    private void setUpViews(View parentView) {
        MainActivity parentActivity = (MainActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();

        parentView.findViewById(R.id.btn_open_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
    }

}
