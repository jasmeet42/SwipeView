package com.jasmeet.swipeviewdemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SwipeView extends LinearLayout {
    private static final long ANIMATION_DURATION = 500;
    private static final int  SWIPE_MIN_DISTANCE = 10;
    private final Activity                      mContext;
    private       ConstraintLayout.LayoutParams mViewLayoutParams;
    private int mCurrentPosition = 0;
    private int                  mNumberOfPages;
    private boolean              isScrolling;
    private OnPageChangeListener mPageChangedListener;

    public interface OnPageChangeListener {
        void onPageChanged(int position);
    }

    public SwipeView(Context context) {
        super(context);
        mContext = (Activity) context;

        setUp();
    }

    public SwipeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = (Activity) context;

        setUp();
    }

    public SwipeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = (Activity) context;

        setUp();

    }

    public void setOnPageChangedListener(OnPageChangeListener onPageChangedListener) {
        mPageChangedListener = onPageChangedListener;
    }

    private void setUp() {
        mViewLayoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mViewLayoutParams.leftMargin = 0;
        setLayoutParams(mViewLayoutParams);

        final GestureDetector gestureDetector = new GestureDetector(mContext, mGestureListener);
        setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    public void addViews(View... views) {
        if (views != null) {
            mNumberOfPages = views.length;
            for (View view : views) {
                view.setLayoutParams(new LinearLayout.LayoutParams(getPageWidth(), ViewGroup.LayoutParams.MATCH_PARENT));
                addView(view);
            }
        }
    }

    private int getPageWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        return dm.widthPixels;
    }


    private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.OnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (isScrolling) {
                return true;
            }
            pageScroll(distanceX);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            return true;
        }
    };


    private void pageScroll(float distanceX) {
        float leftMargin = mViewLayoutParams.leftMargin - distanceX;
        if (leftMargin <= 0 && leftMargin >= -(mNumberOfPages - 1) * getPageWidth() && Math.abs(distanceX) > dptoPx(SWIPE_MIN_DISTANCE)) {
            final int newPos = (int) (mCurrentPosition + distanceX / Math.abs(distanceX));
            scrollToPos(newPos);
        }
    }

    private void scrollToPos(final int newPos) {
        isScrolling = true;
        ValueAnimator animation = ValueAnimator.ofFloat(mViewLayoutParams.leftMargin, -newPos * getPageWidth());
        animation.setDuration(ANIMATION_DURATION);
        animation.start();
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mViewLayoutParams.leftMargin = ((Float) animation.getAnimatedValue()).intValue();
                setLayoutParams(mViewLayoutParams);
            }
        });
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isScrolling = false;

                mCurrentPosition = newPos;
                if (mPageChangedListener != null) {
                    mPageChangedListener.onPageChanged(mCurrentPosition);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private int dptoPx(int dp) {

        DisplayMetrics displayMetrics = mContext.getResources()
                .getDisplayMetrics();

        return (int) (dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));

    }

    public void setCurrentPosition(int position) {
        if (position >= 0 && position < mNumberOfPages) {
            scrollToPos(position);
        }
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    public int getPageCount() {
        return mNumberOfPages;
    }
}
