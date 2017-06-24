package com.eggsy.ui.view;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.eggsy.ui.PullToRefreshView;


public abstract class BaseDrawableRefreshView extends Drawable implements Drawable.Callback, SuperRefreshView {

    private PullToRefreshView mRefreshLayout;

    ImageView mContainerView;

    public BaseDrawableRefreshView(Context context, PullToRefreshView layout) {
        mRefreshLayout = layout;
    }

    public Context getContext() {
        return mRefreshLayout != null ? mRefreshLayout.getContext() : null;
    }

    public PullToRefreshView getRefreshLayout() {
        return mRefreshLayout;
    }


    @Override
    public void setContainerView(ImageView containerView) {
        this.mContainerView = containerView;
        initContainerView(containerView);
    }

    public abstract void initContainerView(ImageView containerView);

    @Override
    public void invalidateDrawable(@NonNull Drawable who) {
        final Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        final Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, what, when);
        }
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        final Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, what);
        }
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }
}
