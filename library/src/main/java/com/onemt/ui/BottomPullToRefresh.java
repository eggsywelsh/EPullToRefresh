package com.onemt.ui;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

import com.onemt.ui.view.SuperRefreshView;

import static com.onemt.ui.PullToRefreshView.MAX_OFFSET_ANIMATION_DURATION;

/**
 * Top refresh component
 *
 * @author chenyongkang
 * @Date 2017/5/27 15:10
 */
public final class BottomPullToRefresh extends BasePullToRefreshData implements BasePullToRefresh {

    private static final String TAG = PullToRefreshView.class.getSimpleName();

    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;

    static final int DRAG_MAX_DISTANCE = 30;

    DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);

    Context mContext;

    Target mTarget;

    ViewGroup mParent;

    public BottomPullToRefresh(Context context, ViewGroup parent, Target target) {
        super(context);
        this.mContext = context;
        this.mTarget = target;
        this.mParent = parent;
    }

    @Override
    public void setRefreshView(SuperRefreshView refreshView) {
        mRefreshView = refreshView;
    }

    @Override
    public boolean hasRefreshView() {
        return mRefreshView != null;
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        if (isRefreshing != refreshing) {
            setRefreshing(refreshing, false /* isNotify */);
        }
    }

    @Override
    public void setOnRefreshListener(PullToRefreshView.OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    @Override
    public void setContainerViewPadding(int left, int top, int right, int bottom) {
        if (mContainerView != null) {
            mContainerView.setPadding(left, top, right, bottom);
        }
    }

    @Override
    public void measureContainerView(int widthMeasureSpec, int heightMeasureSpec) {
        mContainerView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setRefreshViewPercent(float percent, boolean invalidate) {
        mRefreshView.setPercent(percent, invalidate);
    }

    @Override
    public void setRefreshing(boolean refreshing, boolean notify) {
        if (isRefreshing != refreshing) {
            super.isNotify = notify;
            mTarget.ensureTarget();
            super.isRefreshing = refreshing;
            if (isRefreshing) {
                mRefreshView.setPercent(-1f, true);
                animateOffsetToCorrectPosition();
            } else {
                animateOffsetToStartPosition();
            }
        }
    }

    @Override
    public void animateOffsetToStartPosition() {
        from = mTarget.getCurrentOffsetBottom();
        fromDragPercent = mTarget.getCurrentTopDragPercent();
        long animationDuration = Math.abs((long) (MAX_OFFSET_ANIMATION_DURATION * fromDragPercent));

        mAnimateToStartPosition.reset();
        mAnimateToStartPosition.setDuration(animationDuration);
        mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
        mAnimateToStartPosition.setAnimationListener(mToStartListener);
        mContainerView.clearAnimation();
        mContainerView.startAnimation(mAnimateToStartPosition);
    }

    @Override
    public void animateOffsetToCorrectPosition() {
        from = mTarget.getCurrentOffsetBottom();
        fromDragPercent = mTarget.getCurrentTopDragPercent();

        mAnimateToCorrectPosition.reset();
        mAnimateToCorrectPosition.setDuration(MAX_OFFSET_ANIMATION_DURATION);
        mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);
        mContainerView.clearAnimation();
        mContainerView.startAnimation(mAnimateToCorrectPosition);

        if (isRefreshing) {
            mRefreshView.start();
            if (isNotify) {
                if (mOnRefreshListener != null) {
                    mOnRefreshListener.onRefresh();
                }
            }
        } else {
            mRefreshView.stop();
            animateOffsetToStartPosition();
        }
//        mTarget.updatePaddingAndOffset();
    }

    private final Animation mAnimateToCorrectPosition = new Animation() {

        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            int targetBottom;
            int endTarget = -mTarget.getTotalBottomDragDistance();
            targetBottom = (from + (int) ((endTarget - from) * interpolatedTime));
            int offset = targetBottom - mTarget.getTargetViewBottom();

            mTarget.setCurrentTopDragPercent(fromDragPercent - (1.0f + fromDragPercent) * interpolatedTime);
            mRefreshView.setPercent(mTarget.getCurrentTopDragPercent(), false);

            offsetTopAndBottom(offset, false);
        }
    };

    private void moveToStart(float interpolatedTime) {
        int targetBottom = from - (int) (from * interpolatedTime);
        float targetPercent = fromDragPercent * (1.0f - interpolatedTime);
        int offset = targetBottom - mTarget.getCurrentOffsetBottom();

        mTarget.setCurrentTopDragPercent(targetPercent);
        mRefreshView.setPercent(mTarget.getCurrentTopDragPercent(), true);

//        mTarget.moveToStart(targetBottom);

        offsetTopAndBottom(offset, false);
    }

    private Animation.AnimationListener mToStartListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
//            mTarget.setAnimateFinished(false);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mRefreshView.stop();
//            mTarget.updateCurrentOffSetTop();
//            mTarget.setAnimateFinished(true);
        }
    };

    private final Animation mAnimateToStartPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            moveToStart(interpolatedTime);
        }
    };

    @Override
    public void updateRefreshViewLayout(int left, int top, int right, int bottom) {
//        int height = 0;
//        int width = 0;
//        if (mParent != null) {
//            height = mParent.getMeasuredHeight();
//            width = mParent.getMeasuredWidth();
//        }
//        mContainerView.layout(left, height-100, left + width - right, height);

        mContainerView.layout(left, top, right, bottom);
    }

    public void offsetTopAndBottom(int offset, boolean requiresUpdate) {
        Log.d(TAG, "offsetTopAndBottom " + offset);
        mTarget.offsetTopAndBottom(offset);
        if (mContainerView.getDrawable() == null && mRefreshView != null) {
            mContainerView.setImageDrawable(mRefreshView.obtainRefreshDrawable());
            mContainerView.scrollTo(0, -mTarget.getTotalBottomDragDistance());
            Log.d(TAG, "init offsetTopAndBottom");
        }
//        Log.d(TAG, "offsetTopAndBottom " + (Math.abs(mTarget.getCurrentOffsetBottom()) - mTarget.getTotalBottomDragDistance()));
//        mRefreshView.offsetTopAndBottom(10);
//         mRefreshView.offsetTopAndBottom(Math.abs(mTarget.getCurrentOffsetBottom()) - mTarget.getTotalBottomDragDistance());
//        mContainerView.scrollTo(0, offset);
        mContainerView.scrollTo(0, Math.abs(mTarget.getCurrentOffsetBottom()) - mTarget.getTotalBottomDragDistance());
        mTarget.updateCurrentOffsetBottom(offset);
        if (requiresUpdate && android.os.Build.VERSION.SDK_INT < 11) {
            mParent.invalidate();
        }
    }
}
