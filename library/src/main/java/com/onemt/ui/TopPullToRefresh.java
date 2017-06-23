package com.onemt.ui;

import android.content.Context;
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
public final class TopPullToRefresh extends BasePullToRefreshData implements BasePullToRefresh {

    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;

    static final int DRAG_MAX_DISTANCE = 120;

    DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);

    Context mContext;

    Target mTarget;

    ViewGroup mParent;

    public TopPullToRefresh(Context context, ViewGroup parent, Target target) {
        super(context);
        this.mContext = context;
        this.mTarget = target;
        this.mParent = parent;
    }

    @Override
    public void setRefreshView(SuperRefreshView refreshView) {
        mRefreshView = refreshView;
        if (refreshView != null) {
            mContainerView.setImageDrawable(refreshView.obtainRefreshDrawable());
        }
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
                mRefreshView.setPercent(1f, true);
                animateOffsetToCorrectPosition();
            } else {
                animateOffsetToStartPosition();
            }
        }
    }

    @Override
    public void animateOffsetToStartPosition() {
        from = mTarget.getCurrentOffsetTop();
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
        from = mTarget.getCurrentOffsetTop();
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
        mTarget.updatePaddingAndOffset();
    }

    private final Animation mAnimateToCorrectPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            int targetTop;
            int endTarget = mTarget.getTotalTopDragDistance();
            targetTop = (from + (int) ((endTarget - from) * interpolatedTime));
            int offset = targetTop - mTarget.getTargetViewTop();

            mTarget.setCurrentTopDragPercent(fromDragPercent - (fromDragPercent - 1.0f) * interpolatedTime);
            mRefreshView.setPercent(mTarget.getCurrentTopDragPercent(), false);

            offsetTopAndBottom(offset, false);
        }
    };

    private final Animation mAnimateToStartPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            moveToStart(interpolatedTime);
        }
    };

    private void moveToStart(float interpolatedTime) {
        int targetTop = from - (int) (from * interpolatedTime);
        float targetPercent = fromDragPercent * (1.0f - interpolatedTime);
        int offset = targetTop - mTarget.getTargetViewTop();

        mTarget.setCurrentTopDragPercent(targetPercent);
        mRefreshView.setPercent(mTarget.getCurrentTopDragPercent(), true);

        mTarget.moveToStart(targetTop);

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
            mTarget.updateCurrentOffSetTop();
//            mTarget.setAnimateFinished(true);
        }
    };

    public void offsetTopAndBottom(int offset, boolean requiresUpdate) {
        mTarget.offsetTopAndBottom(offset);
        mRefreshView.offsetTopAndBottom(offset);
        mTarget.updateCurrentOffSetTop();
        if (requiresUpdate && android.os.Build.VERSION.SDK_INT < 11) {
            mParent.invalidate();
        }
    }

    @Override
    public void updateRefreshViewLayout(int left, int top, int right, int bottom) {
        mContainerView.layout(left, top, right, bottom);
    }

}
