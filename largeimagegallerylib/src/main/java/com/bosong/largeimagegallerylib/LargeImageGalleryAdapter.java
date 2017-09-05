package com.bosong.largeimagegallerylib;

import android.support.annotation.DrawableRes;
import android.support.v4.view.PagerAdapter;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bosong.frescozoomablelib.zoomable.AbstractAnimatedZoomableController;
import com.bosong.frescozoomablelib.zoomable.DefaultZoomableController;
import com.bosong.frescozoomablelib.zoomable.DoubleTapGestureListener;
import com.bosong.frescozoomablelib.zoomable.ZoomableController;
import com.bosong.frescozoomablelib.zoomable.ZoomableDraweeView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;

import java.util.List;

/**
 * Created by boson on 2016/12/21.
 */

public class LargeImageGalleryAdapter extends PagerAdapter {
    private static final float SWIPE_DOWN_THRETHOLD = 100.F;

    List<String> mData;
    ZoomableDraweeView[] mImageViewList;

    private int mPlaceholderImageResId;
    private int mFailureImageResId;

    private View.OnClickListener mItemClickListener;
    private ZoomableController.OnSwipeDownListener mSwipeDownListener;

    public LargeImageGalleryAdapter(){
        this(null);
    }

    public LargeImageGalleryAdapter(List<String> data){
        setData(data);
    }

    public void setData(List<String> imageUrls){
        setData(imageUrls, 0, 0);
    }

    public void setData(List<String> imageUrls, @DrawableRes int placeholderImageResId, @DrawableRes int failureImageResId){
        if(imageUrls != null){
            mData = imageUrls;
            mImageViewList = new ZoomableDraweeView[mData.size()];
        }
        mPlaceholderImageResId = placeholderImageResId;
        mFailureImageResId = failureImageResId;
    }

    public void setOnItemClickListener(View.OnClickListener listener){
        this.mItemClickListener = listener;
    }

    public void setSwipeDownListener(ZoomableController.OnSwipeDownListener listener) {
        mSwipeDownListener = listener;
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        if(mData != null && mData.size() > position){
            ZoomableDraweeView zoomableDraweeView = null;
            if(mImageViewList != null && mImageViewList.length > position && mImageViewList[position] != null){
                zoomableDraweeView = mImageViewList[position];

            }else{
                zoomableDraweeView = new ZoomableDraweeView(container.getContext());
                zoomableDraweeView.setAllowTouchInterceptionWhileZoomed(true);
                // needed for double tap to zoom
                zoomableDraweeView.setIsLongpressEnabled(false);
                zoomableDraweeView.setSwipeDownListener(mSwipeDownListener);
                final ZoomableDraweeView finalZoomableDraweeView = zoomableDraweeView;
                zoomableDraweeView.setTapListener(new DoubleTapGestureListener(finalZoomableDraweeView){
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        if(mItemClickListener != null){
                            mItemClickListener.onClick(finalZoomableDraweeView);
                        }
                        return super.onSingleTapConfirmed(e);
                    }
                });
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setUri(mData.get(position % mData.size()))
                        .build();
                zoomableDraweeView.setController(controller);
                GenericDraweeHierarchyBuilder builder =
                        new GenericDraweeHierarchyBuilder(container.getResources());
                GenericDraweeHierarchy hierarchy = builder
                        .setFadeDuration(300).setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                        .build();
                if(mPlaceholderImageResId > 0){
                    hierarchy.setPlaceholderImage(mPlaceholderImageResId, ScalingUtils.ScaleType.FIT_CENTER);
                }
                if(mFailureImageResId > 0){
                    hierarchy.setFailureImage(mFailureImageResId, ScalingUtils.ScaleType.FIT_CENTER);
                }
                zoomableDraweeView.setHierarchy(hierarchy);

                mImageViewList[position] = zoomableDraweeView;
            }
            container.addView(zoomableDraweeView);

            return zoomableDraweeView;
        }

        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if(mImageViewList != null && mImageViewList.length > position && mImageViewList[position] != null){
            ZoomableDraweeView imageView = mImageViewList[position];
            container.removeView(imageView);
        }
    }

    public ZoomableDraweeView getItem(int position){
        if(position > -1 && mImageViewList != null && mImageViewList.length > position){
            return mImageViewList[position];
        }
        return null;
    }
}
