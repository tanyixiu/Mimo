package com.tanyixiu.mimo.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tanyixiu.mimo.R;
import com.tanyixiu.mimo.performance.BitmapCache;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by tanyixiu on 2015/7/11.
 */
public class HomeFragment extends Fragment {
    private RequestQueue mRequestQueue;
    private OneEntity oneEntity = new OneEntity();
    private OneItemViewHolder oneItemViewHolder;

    public static HomeFragment getNewInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_home, container, false);

        mRequestQueue = Volley.newRequestQueue(this.getActivity());

        oneItemViewHolder = new OneItemViewHolder();
        oneItemViewHolder.imgShow = (NetworkImageView) parentView.findViewById(R.id.one_img_show);
        oneItemViewHolder.tvAuthor = (TextView) parentView.findViewById(R.id.one_tv_author);
        oneItemViewHolder.tvDay = (TextView) parentView.findViewById(R.id.one_tv_day);
        oneItemViewHolder.tvNumber = (TextView) parentView.findViewById(R.id.one_tv_num);
        oneItemViewHolder.tvYeahMonth = (TextView) parentView.findViewById(R.id.one_tv_monthyear);
        oneItemViewHolder.tvQuote = (TextView) parentView.findViewById(R.id.one_tv_quote);
        oneItemViewHolder.tvTitle = (TextView) parentView.findViewById(R.id.one_tv_title);

        test();
        return parentView;
    }


    private void test() {
        StringRequest request = new StringRequest("http://caodan.org/100-photo.html", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                parseHtml(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        mRequestQueue.add(request);
    }


    private void parseHtml(String s) {
        Document document = Jsoup.parse(s);
        Element content = document.getElementById("content");

        Element day = content.getElementsByClass("day").get(0);
        oneEntity.day = day.childNode(0).outerHtml();
        String monthYear = day.parent().childNode(1).outerHtml();
        oneEntity.month = monthYear.split("/")[0];
        oneEntity.year = monthYear.split("/")[1];

        Element title = content.getElementsByClass("entry-title").get(0);
        String number_title = title.childNode(0).outerHtml();
        oneEntity.number = number_title.split(" ")[0];
        oneEntity.title = number_title.split(" ")[1];


        Element img = content.getElementsByTag("img").get(0);
        oneEntity.imgurl = img.attributes().get("src");


        Element quote = content.getElementsByTag("blockquote").get(0);
        oneEntity.quote = quote.childNode(0).childNode(0).outerHtml();

        Element title_author = quote.parent().getElementsByTag("p").get(1);
        oneEntity.author = title_author.childNode(2).outerHtml();


        Log.d("YI", "day:" + oneEntity.day);
        Log.d("YI", "month:" + oneEntity.month);
        Log.d("YI", "year:" + oneEntity.year);
        Log.d("YI", "number:" + oneEntity.number);
        Log.d("YI", "title:" + oneEntity.title);
        Log.d("YI", "imgurl:" + oneEntity.imgurl);
        Log.d("YI", "quote:" + oneEntity.quote);
        Log.d("YI", "author:" + oneEntity.author);

        oneItemViewHolder.tvDay.setText(oneEntity.day);
        oneItemViewHolder.tvYeahMonth.setText(oneEntity.month + "," + oneEntity.year);
        oneItemViewHolder.tvNumber.setText(oneEntity.number);
        oneItemViewHolder.tvTitle.setText(oneEntity.title);
        oneItemViewHolder.tvQuote.setText(oneEntity.quote);
        oneItemViewHolder.tvAuthor.setText(oneEntity.author);


        ImageLoader imageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
        oneItemViewHolder.imgShow.setImageUrl(oneEntity.imgurl, imageLoader);

    }

    class OneEntity {
        public String day;
        public String month;
        public String year;
        public String number;
        public String title;
        public String author;
        public String quote;
        public String imgurl;
    }

    class OneItemViewHolder {
        public TextView tvNumber;
        public NetworkImageView imgShow;
        public TextView tvTitle;
        public TextView tvAuthor;
        public TextView tvDay;
        public TextView tvYeahMonth;
        public TextView tvQuote;
    }

}