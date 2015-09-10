package com.tanyixiu.mimo.utils;

import android.text.TextUtils;

import com.tanyixiu.mimo.entities.OneItemEntity;
import com.tanyixiu.mimo.moduls.OneItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by tanyixiu on 2015/7/15.
 */
public class OneItemParser {

    public static OneItemEntity parser(String html) throws Exception {
        if (TextUtils.isEmpty(html)) {
            throw new NullPointerException("response html is null");
        }
        OneItemEntity entity = new OneItemEntity();

        Document document = Jsoup.parse(html);
        Element content = document.getElementById("content");

        Element day = content.getElementsByClass("day").get(0);
        entity.setDay(day.childNode(0).outerHtml());

        String monthYear = day.parent().childNode(1).outerHtml();
        entity.setMonth(monthYear.split("/")[0]);
        entity.setYear(monthYear.split("/")[1]);

        Element title = content.getElementsByClass("entry-title").get(0);
        String number_title = title.childNode(0).outerHtml();
        entity.setNumber(number_title.split(" ")[0]);
        entity.setTitle(number_title.split(" ")[1]);

        Element img = content.getElementsByTag("img").get(0);
        entity.setImgurl(img.attributes().get("src"));

        Element quote = content.getElementsByTag("blockquote").get(0);
        entity.setQuote(quote.childNode(0).childNode(0).outerHtml());

        Element title_author = quote.parent().getElementsByTag("p").get(1);
        entity.setAuthor(title_author.childNode(2).outerHtml());

        int id = Integer.valueOf(entity.getNumber().replace("VOL.", ""));
        entity.setId(id);
        return entity;
    }


    public static OneItem parserOne(String html) throws Exception {
        if (TextUtils.isEmpty(html)) {
            throw new NullPointerException("response html is null");
        }
        OneItem oneItem = new OneItem();

        Document document = Jsoup.parse(html);
        Element content = document.getElementById("content");

        Element day = content.getElementsByClass("day").get(0);
        oneItem.setDay(day.childNode(0).outerHtml());

        String monthYear = day.parent().childNode(1).outerHtml();
        oneItem.setMonth(monthYear.split("/")[0]);
        oneItem.setYear(monthYear.split("/")[1]);

        Element title = content.getElementsByClass("entry-title").get(0);
        String number_title = title.childNode(0).outerHtml();
        oneItem.setNumber(number_title.split(" ")[0]);
        oneItem.setTitle(number_title.split(" ")[1]);

        Element img = content.getElementsByTag("img").get(0);
        oneItem.setImgurl(img.attributes().get("src"));

        Element quote = content.getElementsByTag("blockquote").get(0);
        oneItem.setQuote(quote.childNode(0).childNode(0).outerHtml());

        Element title_author = quote.parent().getElementsByTag("p").get(1);
        oneItem.setAuthor(title_author.childNode(2).outerHtml());

        int id = Integer.valueOf(oneItem.getNumber().replace("VOL.", ""));
        oneItem.setId(id);

        if (null == oneItem) {
            throw new Exception("OneItemParser parsed failed");
        }
        return oneItem;
    }
}
