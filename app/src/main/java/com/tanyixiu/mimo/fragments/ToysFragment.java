package com.tanyixiu.mimo.fragments;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.tanyixiu.mimo.R;
import com.tanyixiu.mimo.utils.ToastUtils;
import com.tanyixiu.widgets.refreshlistview.DropDownListView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

public class ToysFragment extends Fragment {

    private LinkedList<String> listItems = null;
    private DropDownListView listView = null;
    private ArrayAdapter<String> adapter;

    private String[] mStrings = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",};

    public static final int MORE_DATA_MAX_COUNT = 3;
    public int moreDataCount = 0;

    public static ToysFragment getNewInstance() {
        return new ToysFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_toys, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (DropDownListView) getActivity().findViewById(R.id.list_view);
        listView.setOnDropDownListener(new DropDownListView.OnDropDownListener() {

            @Override
            public void onDropDown() {
                new GetDataTask(true).execute();
            }
        });

        listView.setOnBottomListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new GetDataTask(false).execute();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtils.show(getActivity(), R.string.drop_down_tip);
            }
        });

        // listView.setShowFooterWhenNoMore(true);
        listItems = new LinkedList<String>();
        listItems.addAll(Arrays.asList(mStrings));
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        private boolean isDropDown;

        public GetDataTask(boolean isDropDown) {
            this.isDropDown = isDropDown;
        }

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mStrings;
        }

        @Override
        protected void onPostExecute(String[] result) {

            if (isDropDown) {
                listItems.addFirst("Added after drop down");
                adapter.notifyDataSetChanged();

                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
                listView.onDropDownComplete(getString(R.string.update_at) + dateFormat.format(new Date()));
            } else {
                moreDataCount++;
                listItems.add("Added after on bottom");
                adapter.notifyDataSetChanged();

                if (moreDataCount >= MORE_DATA_MAX_COUNT) {
                    listView.setHasMore(false);
                }
                listView.onBottomComplete();
            }

            super.onPostExecute(result);
        }
    }
}
