package com.eggsy.ui.sample.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.eggsy.ui.PullToRefreshView;
import com.eggsy.ui.view.BaseDrawableRefreshView;

/**
 * Created by eggsy on 2017/7/13.
 */

public class CamelRefreshView extends BaseDrawableRefreshView implements Animatable {



    public CamelRefreshView(Context context, final PullToRefreshView parent) {

        super(context, parent);

//        this.mParent = parent;
//
//        this.mPaint = new Paint();
//
//        setupDrawable();
//
//        setupAnimations();
//
//        parent.post(new Runnable() {
//            @Override
//            public void run() {
//                initiateDimens(parent.getWidth());
//            }
//        });
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

    }

    @Override
    public void initContainerView(ImageView containerView) {

    }

    @Override
    public void setPercent(float percent, boolean invalidate) {

    }

    @Override
    public void offsetTopAndBottom(int offset) {

    }

    @Override
    public Drawable obtainRefreshDrawable() {
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
