package com.bosong.commentgallerylib;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * SimpleDraweeView with same width and height
 * Created by bosong on 2017/1/6.
 */

public class SquareDraweeView extends SimpleDraweeView {
    public SquareDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public SquareDraweeView(Context context) {
        super(context);
    }

    public SquareDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
