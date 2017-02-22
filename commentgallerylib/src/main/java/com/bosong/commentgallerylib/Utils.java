package com.bosong.commentgallerylib;

import android.content.Context;

/**
 * Created by bosong on 2016/12/23.
 */

public class Utils {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
