package com.tanyixiu.mimo.moduls;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.tanyixiu.mimo.R;

/**
 * Created by Mimo on 2015/9/11.
 */
@Table(name = "bookitem", id = "tb_bookitem_id")
public class BookItem extends Model implements Parcelable {

    private static final String IMAGE_URL = "/sdcard/mimo/images/books/%s.png";

    public class BooKState {
        public static final int NOT_READ = 0;
        public static final int READING = 1;
        public static final int FINISH_READING = 2;
    }

    @Column(name = "id", notNull = true, unique = true)
    private String id;

    @Column(name = "name", notNull = true)
    private String name;

    @Column(name = "digest", notNull = true)
    private String digest;

    @Column(name = "author", notNull = true)
    private String author;

    @Column(name = "state", notNull = true)
    private int state;

    @Column(name = "coverurl")
    private String coverUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String createUrl(String filename) {
        return String.format(IMAGE_URL, this.id);
    }

    public boolean isEqual(BookItem item) {
        if (!this.id.equals(item.getId())) {
            return false;
        }
        if (!this.name.equals(item.getName())) {
            return false;
        }
        if (!this.author.equals(item.getAuthor())) {
            return false;
        }
        if (!this.digest.equals(item.getDigest())) {
            return false;
        }
        if (!this.coverUrl.equals(item.getCoverUrl())) {
            return false;
        }
        if (this.state != item.getState()) {
            return false;
        }
        return true;
    }

    public int getStateColor() {
        switch (this.state) {
            case BookItem.BooKState.NOT_READ:
                return R.color.bkg_color_orange;
            case BookItem.BooKState.READING:
                return R.color.bkg_color_blue;
            case BookItem.BooKState.FINISH_READING:
            default:
                return R.color.bkg_color_green;
        }
    }

    public String getStateName() {
        switch (this.state) {
            case BookItem.BooKState.NOT_READ:
                return "未读";
            case BookItem.BooKState.READING:
                return "正在读";
            case BookItem.BooKState.FINISH_READING:
                return "已读";
            default:
                return "";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.digest);
        dest.writeString(this.author);
        dest.writeInt(this.state);
        dest.writeString(this.coverUrl);
    }

    public BookItem() {
    }

    protected BookItem(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.digest = in.readString();
        this.author = in.readString();
        this.state = in.readInt();
        this.coverUrl = in.readString();
    }

    public static final Creator<BookItem> CREATOR = new Creator<BookItem>() {
        public BookItem createFromParcel(Parcel source) {
            return new BookItem(source);
        }

        public BookItem[] newArray(int size) {
            return new BookItem[size];
        }
    };
}