package com.tanyixiu.mimo.holder;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanyixiu.mimo.R;
import com.tanyixiu.mimo.entities.OneItemEntity;

/**
 * Created by tanyixiu on 2015/7/19.
 */
public class OneItemViewHolder {

    private TextView tvNumber;
    private ImageView imgShow;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvDay;
    private TextView tvYeahMonth;
    private TextView tvQuote;

    public OneItemViewHolder(View contentView) {
        this.tvNumber = (TextView) contentView.findViewById(R.id.one_tv_num);
        this.imgShow = (ImageView) contentView.findViewById(R.id.one_img_show);
        this.tvTitle = (TextView) contentView.findViewById(R.id.one_tv_title);
        this.tvAuthor = (TextView) contentView.findViewById(R.id.one_tv_author);
        this.tvDay = (TextView) contentView.findViewById(R.id.one_tv_day);
        this.tvYeahMonth = (TextView) contentView.findViewById(R.id.one_tv_monthyear);
        this.tvQuote = (TextView) contentView.findViewById(R.id.one_tv_quote);
    }

    public void bindData(OneItemEntity entity, Bitmap bitmap) {
        this.tvNumber.setText(entity.getNumber());
        this.tvTitle.setText(entity.getTitle());
        this.tvAuthor.setText(entity.getAuthor());
        this.tvDay.setText(entity.getDay());
        this.tvYeahMonth.setText(entity.getMonth() + "," + entity.getYear());
        this.tvQuote.setText(entity.getQuote());
        this.imgShow.setTag(entity.getImgurl());
        if (null != bitmap) {
            this.imgShow.setImageBitmap(bitmap);
        } else {
            this.imgShow.setImageResource(R.drawable.empty_photo);
        }
    }

    public void clearData() {
        this.tvNumber.setText("");
        this.tvTitle.setText("");
        this.tvAuthor.setText("");
        this.tvDay.setText("");
        this.tvYeahMonth.setText("");
        this.tvQuote.setText("");
        this.imgShow.setTag(null);
    }
}
