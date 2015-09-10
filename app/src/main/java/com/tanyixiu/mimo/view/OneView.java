package com.tanyixiu.mimo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tanyixiu.mimo.R;
import com.tanyixiu.mimo.moduls.OneItem;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Mimo on 2015/8/28.
 */
public class OneView extends FrameLayout {

    private int mTagId;
    private ViewHolder mViewHolder;

    public OneView(Context context) {
        super(context);
        initView();
    }

    public OneView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public OneView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.view_one, this);
        mViewHolder = new ViewHolder(v);
    }

    public void setTagId(int tagId) {
        mTagId = tagId;
    }

    public int getTagId() {
        return mTagId;
    }

    public void bindData(OneItem oneItem) {
        if (oneItem.getId() != mTagId) {
            mViewHolder.clearData();
        }
        mViewHolder.bindData(oneItem);
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'view_one.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    protected static class ViewHolder {
        @InjectView(R.id.one_tv_num)
        TextView mOneTvNum;
        @InjectView(R.id.one_img_show)
        ImageView mOneImgShow;
        @InjectView(R.id.one_tv_title)
        TextView mOneTvTitle;
        @InjectView(R.id.one_tv_author)
        TextView mOneTvAuthor;
        @InjectView(R.id.one_tv_day)
        TextView mOneTvDay;
        @InjectView(R.id.one_tv_quote)
        TextView mOneTvQuote;
        @InjectView(R.id.one_tv_monthyear)
        TextView mOneTvMonthyear;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

        public void bindData(OneItem oneItem) {
            if (null == oneItem) {
                return;
            }
            this.mOneTvNum.setText(oneItem.getNumber());
            this.mOneTvTitle.setText(oneItem.getTitle());
            this.mOneTvAuthor.setText(oneItem.getAuthor());
            this.mOneTvDay.setText(oneItem.getDay());
            this.mOneTvMonthyear.setText(oneItem.getMonth() + "," + oneItem.getYear());
            this.mOneTvQuote.setText(oneItem.getQuote());
            String imgUrl = oneItem.getImgurl();
            this.mOneImgShow.setTag(imgUrl);

            ImageLoader.getInstance().displayImage(imgUrl, this.mOneImgShow, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    RotateAnimation animation = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setRepeatCount(Animation.INFINITE);
                    animation.setRepeatMode(Animation.RESTART);
                    animation.setInterpolator(new LinearInterpolator());
                    animation.setDuration(1000);
                    mOneImgShow.startAnimation(animation);
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    mOneImgShow.clearAnimation();
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    mOneImgShow.clearAnimation();
                    if (s.equals(view.getTag().toString())) {
                        mOneImgShow.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    mOneImgShow.clearAnimation();
                }
            });
        }

        public void clearData() {
            this.mOneTvNum.setText("");
            this.mOneTvTitle.setText("");
            this.mOneTvAuthor.setText("");
            this.mOneTvDay.setText("");
            this.mOneTvMonthyear.setText("");
            this.mOneTvQuote.setText("");
            this.mOneImgShow.setTag(null);
        }
    }

}
