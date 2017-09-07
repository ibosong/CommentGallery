package com.bosong.largeimagegallerylib;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.bosong.frescozoomablelib.zoomable.ZoomableController;
import com.bosong.frescozoomablelib.zoomable.ZoomableDraweeView;

import java.util.List;

/**
 *
 * Created by bo.song on 2016/12/22.
 */

public class LargeImageGallery extends FrameLayout {

    private static final int LAST_INDEX_DEFAULT_VALUE = -1;
    private static final int CURRENT_INDEX_DEFAULT_VALUE = 0;

    private ViewPager mViewPager;
    private Context mContext;

    private LargeImageGalleryAdapter mAdapter;

    private int mCurrentIndex = CURRENT_INDEX_DEFAULT_VALUE;

    private OnSelectionChangedListener mOnSelectionChangedListener;
    private OnItemClickListener mOnItemClickListener;

    private ViewPager.SimpleOnPageChangeListener simpleOnPageChangeListener = new SimpleOnPageChangeListener(){
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);

            if(mOnSelectionChangedListener != null){
                mOnSelectionChangedListener.onSelectionChanged(mCurrentIndex, position);
            }

            mCurrentIndex = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    public LargeImageGallery(@NonNull Context context) {
        this(context, null);
    }

    public LargeImageGallery(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LargeImageGallery(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initData();
    }

    public ZoomableDraweeView getCurrentItemView() {
        return mAdapter.getItem(mCurrentIndex);
    }

    private void initView(){
        mViewPager = new ViewPager(mContext);
        mViewPager.addOnPageChangeListener(simpleOnPageChangeListener);
        addView(mViewPager, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
    }

    private void initData(){
        mAdapter = new LargeImageGalleryAdapter();
        mViewPager.setAdapter(mAdapter);
    }

    public void setData(List<String> urls){
        if(urls != null){
            mAdapter.setData(urls, R.drawable.placeholder, R.drawable.placeholder);
            if(urls != null && urls.size() > 0 && mOnSelectionChangedListener != null){
                mOnSelectionChangedListener.onSelectionChanged(LAST_INDEX_DEFAULT_VALUE, CURRENT_INDEX_DEFAULT_VALUE);
            }
            mAdapter.notifyDataSetChanged();
        }

    }

    public void setCurrentItem(int position){
        if(mAdapter != null && mAdapter.getCount() > 0){
            if(position < 0 || position > mAdapter.getCount()){
                position = 0;
            }
            mViewPager.setCurrentItem(position);
        }
    }

    public void setOnImageSelectedListener(OnSelectionChangedListener listener){
        this.mOnSelectionChangedListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        if(mAdapter != null && listener != null){
            this.mOnItemClickListener = listener;
            mAdapter.setOnItemClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(mCurrentIndex);
                }
            });
        }
    }

    public void setOnSwipeDownListener(ZoomableController.OnSwipeDownListener listener) {
        if(mAdapter != null) {
            mAdapter.setSwipeDownListener(listener);
        }
    }

    public interface OnSelectionChangedListener{
        void onSelectionChanged(int lastIndex, int currentIndex);
    }

    public interface OnItemClickListener {
        void onItemClick(int currentIndex);
    }
}
