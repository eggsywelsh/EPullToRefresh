package com.onemt.ui.view;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * super refreshing view
 *
 * @author chenyongkang
 * @Date 2017/6/1 16:20
 */
public interface SuperRefreshView extends Animatable {

    void setPercent(float percent, boolean invalidate);

    void offsetTopAndBottom(int offset);

    Drawable obtainRefreshDrawable();

    void setContainerView(ImageView containerView);

}
