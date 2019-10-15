package com.jasmeet.swipeviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

public class MainActivity extends Activity implements SwipeView.OnPageChangeListener {

    private SwipeView mSwipeView;
    private View      mLeftNavigationArrow;
    private View      mRightNavigationArrow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipeView = findViewById(R.id.swipe_view);
        mLeftNavigationArrow = findViewById(R.id.navigate_left);
        mRightNavigationArrow = findViewById(R.id.navigate_right);

        setUpView();
    }

    private void setUpView() {
        mSwipeView.addViews(LayoutInflater.from(this).inflate(R.layout.view1, mSwipeView, false),
                LayoutInflater.from(this).inflate(R.layout.view2, mSwipeView, false));

        mSwipeView.setOnPageChangedListener(this);

        if (mSwipeView.getPageCount() > 1) {
            mRightNavigationArrow.setVisibility(View.VISIBLE);
        }

        mLeftNavigationArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.setCurrentPosition(mSwipeView.getCurrentPosition() - 1);
            }
        });
        mRightNavigationArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.setCurrentPosition(mSwipeView.getCurrentPosition() + 1);
            }
        });
    }


    @Override
    public void onPageChanged(int position) {
        if (position == 0) {
            mLeftNavigationArrow.setVisibility(View.GONE);
            mRightNavigationArrow.setVisibility(View.VISIBLE);
        } else if (position < mSwipeView.getPageCount() - 1) {
            mLeftNavigationArrow.setVisibility(View.VISIBLE);
            mRightNavigationArrow.setVisibility(View.VISIBLE);
        } else {
            mLeftNavigationArrow.setVisibility(View.VISIBLE);
            mRightNavigationArrow.setVisibility(View.GONE);
        }
    }
}
