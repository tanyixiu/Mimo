package com.tanyixiu.widgets.slidedrawer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.tanyixiu.mimo.R;

/**
 * Created by tanyixiu on 2015/7/11.
 */
public class SlideDrawer extends FrameLayout {

    private static final int PRESSED_DOWN = 1;
    private static final int PRESSED_DONE = 2;
    private static final int PRESSED_MOVE_HORIZONTAL = 3;
    private static final int PRESSED_MOVE_VERTICAL = 4;

    private View mMenuView;
    private ScrollView mScrollViewMenu;
    private ImageView mImageViewBackground;
    private SlideDrawerAttacher mSlideDrawerAttacher;

    private OnSlideDrawerListener menuListener;

    private float lastRawX;
    private boolean isOpened;
    private float mScaleValue = 0.6f;
    private int pressedState = PRESSED_DOWN;

    public SlideDrawer(Context context) {
        super(context);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widgets_slidedrawer, this);

        mScrollViewMenu = (ScrollView) findViewById(R.id.sv_menu);
        mImageViewBackground = (ImageView) findViewById(R.id.iv_background);
    }

    public void attachToActivity(Activity activity) {
        ViewGroup viewDecor = (ViewGroup) activity.getWindow().getDecorView();//root view
        View mContent = viewDecor.getChildAt(0);
        viewDecor.removeViewAt(0);

        mSlideDrawerAttacher = new SlideDrawerAttacher(activity);
        mSlideDrawerAttacher.setContent(mContent);
        addView(mSlideDrawerAttacher);

        ViewGroup parent = (ViewGroup) mScrollViewMenu.getParent();
        parent.removeView(mScrollViewMenu);

        viewDecor.addView(this, 0);
    }

    public void setBackground(int imageResource) {
        mImageViewBackground.setImageResource(imageResource);
    }

    public void setMenuView(View menuView) {
        mMenuView = menuView;
        mScrollViewMenu.removeAllViews();
        mScrollViewMenu.addView(mMenuView);
    }

    public void setMenuListener(OnSlideDrawerListener menuListener) {
        this.menuListener = menuListener;
    }

    public OnSlideDrawerListener getMenuListener() {
        return menuListener;
    }

    public void openSlideDrawer() {

        isOpened = true;
        setPivot();
        AnimatorSet scaleDown_activity = buildScaleDownAnimation(mSlideDrawerAttacher,
                mScaleValue, mScaleValue);
        AnimatorSet alpha_menu = buildMenuAnimation(mScrollViewMenu, 1.0f);
        scaleDown_activity.addListener(animationListener);
        scaleDown_activity.playTogether(alpha_menu);
        scaleDown_activity.start();
    }

    public void closeSlideDrawer() {

        isOpened = false;
        AnimatorSet scaleUp_activity = buildScaleUpAnimation(mSlideDrawerAttacher, 1.0f, 1.0f);
        AnimatorSet alpha_menu = buildMenuAnimation(mScrollViewMenu, 0.0f);
        scaleUp_activity.addListener(animationListener);
        scaleUp_activity.playTogether(alpha_menu);
        scaleUp_activity.start();
    }

    private void setPivot() {
        DisplayMetrics displayMetrics = getScreenDisplayMetrics();
        float pivotX = displayMetrics.widthPixels * 1.5f;
        float pivotY = displayMetrics.heightPixels * 0.5f;

        mSlideDrawerAttacher.setPivotX(pivotX);
        mSlideDrawerAttacher.setPivotY(pivotY);

        mScrollViewMenu.setPivotX(0);
        mScrollViewMenu.setPivotY(0);
    }

    public boolean isOpened() {
        return isOpened;
    }

    private OnClickListener viewActivityOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isOpened()) closeSlideDrawer();
        }
    };

    private Animator.AnimatorListener animationListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            if (isOpened()) {
                showScrollViewMenu(mScrollViewMenu);
                if (menuListener != null)
                    menuListener.openDrawer();
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            // reset the view;
            if (isOpened()) {
                mSlideDrawerAttacher.setTouchDisable(true);
                mSlideDrawerAttacher.setOnClickListener(viewActivityOnClickListener);
            } else {
                mSlideDrawerAttacher.setTouchDisable(false);
                mSlideDrawerAttacher.setOnClickListener(null);
                hideScrollViewMenu(mScrollViewMenu);
                if (menuListener != null)
                    menuListener.closeDrawer();
            }
        }
    };

    private AnimatorSet buildScaleDownAnimation(View target, float targetScaleX, float targetScaleY) {

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
        );

        scaleDown.setInterpolator(new DecelerateInterpolator());
        scaleDown.setDuration(250);
        return scaleDown;
    }

    private AnimatorSet buildScaleUpAnimation(View target, float targetScaleX, float targetScaleY) {

        AnimatorSet scaleUp = new AnimatorSet();
        scaleUp.playTogether(
                ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
                ObjectAnimator.ofFloat(target, "scaleY", targetScaleY)
        );

        scaleUp.setDuration(250);
        return scaleUp;
    }

    private AnimatorSet buildMenuAnimation(View target, float alpha) {

        AnimatorSet alphaAnimation = new AnimatorSet();
        alphaAnimation.playTogether(
                ObjectAnimator.ofFloat(target, "alpha", alpha),
                ObjectAnimator.ofFloat(target, "rotationY", (1 - alpha) * 90.0f)
        );
        alphaAnimation.setInterpolator(new AccelerateInterpolator());
        alphaAnimation.setDuration(250);
        return alphaAnimation;
    }

    private float getTargetScale(float currentRawX) {

        float scaleFloatX =
                ((currentRawX - lastRawX) / getScreenDisplayMetrics().widthPixels) * 0.75f;

        float targetScale = mSlideDrawerAttacher.getScaleX() - scaleFloatX;
        targetScale = targetScale > 1.0f ? 1.0f : targetScale;
        targetScale = targetScale < 0.5f ? 0.5f : targetScale;
        return targetScale;
    }

    private float lastActionDownX, lastActionDownY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentActivityScaleX = mSlideDrawerAttacher.getScaleX();
        if (currentActivityScaleX == 1.0f) {
            setPivot();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastActionDownX = ev.getX();
                lastActionDownY = ev.getY();
                pressedState = PRESSED_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                if (pressedState != PRESSED_DOWN && pressedState != PRESSED_MOVE_HORIZONTAL)
                    break;

                int xOffset = (int) (ev.getX() - lastActionDownX);
                int yOffset = (int) (ev.getY() - lastActionDownY);

                if (pressedState == PRESSED_DOWN) {
                    if (Math.abs(yOffset) > 100) {
                        pressedState = PRESSED_MOVE_VERTICAL;
                        break;
                    }
                    if (Math.abs(xOffset) > 5) {
                        pressedState = PRESSED_MOVE_HORIZONTAL;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }
                } else if (pressedState == PRESSED_MOVE_HORIZONTAL) {
                    if (currentActivityScaleX < 0.95) {
                        showScrollViewMenu(mScrollViewMenu);
                    }
                    float targetScale = getTargetScale(ev.getRawX());
                    float targetAlpha = (1 - targetScale) * 2.0f;
                    float targetRotaion = (1 - targetAlpha) * 90.0f;

                    mSlideDrawerAttacher.setScaleX(targetScale);
                    mSlideDrawerAttacher.setScaleY(targetScale);
                    mScrollViewMenu.setAlpha(targetAlpha);
                    mScrollViewMenu.setRotationY(targetRotaion);

                    lastRawX = ev.getRawX();
                    return true;
                }

                break;

            case MotionEvent.ACTION_UP:

                if (pressedState != PRESSED_MOVE_HORIZONTAL) {
                    break;
                }
                pressedState = PRESSED_DONE;
                if (isOpened()) {
                    if (currentActivityScaleX > 0.56f)
                        closeSlideDrawer();
                    else
                        openSlideDrawer();
                } else {
                    if (currentActivityScaleX < 0.94f) {
                        openSlideDrawer();
                    } else {
                        closeSlideDrawer();
                    }
                }
                break;
        }
        lastRawX = ev.getRawX();
        return super.dispatchTouchEvent(ev);
    }

    private DisplayMetrics getScreenDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public void setScaleValue(float scaleValue) {
        this.mScaleValue = scaleValue;
    }

    public interface OnSlideDrawerListener {

        void openDrawer();

        void closeDrawer();
    }

    private void showScrollViewMenu(ScrollView scrollViewMenu) {
        if (scrollViewMenu != null && scrollViewMenu.getParent() == null) {
            addView(scrollViewMenu);
        }
    }

    private void hideScrollViewMenu(ScrollView scrollViewMenu) {
        if (scrollViewMenu != null && scrollViewMenu.getParent() != null) {
            removeView(scrollViewMenu);
        }
    }
}
