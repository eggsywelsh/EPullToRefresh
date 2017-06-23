package com.onemt.ui;

import android.content.Context;
import android.widget.ImageView;

import com.onemt.ui.view.SuperRefreshView;

/**
 * Pull to refresh's data
 *
 * @author chenyongkang
 * @Date 2017/5/27 15:18
 */
public class BasePullToRefreshData {

    /**
     * Wrapper animation drawable or wrapper a common drawable
     */
    ImageView mContainerView;

    SuperRefreshView mRefreshView;

    /**
     * record the position where the animation start
     */
    int from;

    /**
     * record the percent which is current drag
     */
    float fromDragPercent;

    /**
     * Whether is notify the {@link PullToRefreshView.OnRefreshListener} to refreshing
     */
    boolean isNotify;

    /**
     * set current refresh view is enable to refresh
     */
    private boolean enableRefresh = true;

    /**
     * Whether the view is refreshing
     * true is refreshing
     * false is not refreshing
     */
    boolean isRefreshing;

    PullToRefreshView.OnRefreshListener mOnRefreshListener;

    public BasePullToRefreshData(Context mContext) {
        this.mContainerView = new ImageView(mContext);
    }

    public ImageView getContainerView() {
        return mContainerView;
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public void setIsRefreshing(boolean isRrefreshing) {
        this.isRefreshing = isRrefreshing;
    }

    public void setOnRefreshListener(PullToRefreshView.OnRefreshListener onRefreshListener) {
        this.mOnRefreshListener = onRefreshListener;
    }

    public boolean isEnableRefresh() {
        return enableRefresh;
    }

    public void setEnableRefresh(boolean enableRefresh) {
        this.enableRefresh = enableRefresh;
    }
}
