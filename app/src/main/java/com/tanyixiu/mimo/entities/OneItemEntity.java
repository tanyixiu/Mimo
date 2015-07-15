package com.tanyixiu.mimo.entities;

/**
 * Created by Administrator on 2015/7/15.
 */
public class OneItemEntity {

    private String day;
    private String month;
    private String year;
    private String number;
    private String title;
    private String author;
    private String quote;
    private String imgurl;

    private int uniqueId;

    public int getUniqueId() {
        return uniqueId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
        try {
            uniqueId = Integer.valueOf(number.split(".")[1]);
        } catch (Exception ex) {

        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
