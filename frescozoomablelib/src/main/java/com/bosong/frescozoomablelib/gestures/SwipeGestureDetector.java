package com.bosong.frescozoomablelib.gestures;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by boson on 2017/8/31.
 */
@Deprecated
public class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
    private static final String TAG = "SwipeGestureDetector";
    private OnSwipeListener mSwipeListener;
    private boolean mInThisGesture;
    private float mStartX;
    private float mStartY;
    private float mCurrentX;
    private float mCurrentY;
    public SwipeGestureDetector(OnSwipeListener swipeListener) {
        mSwipeListener = swipeListener;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = mCurrentX = event.getX();
                mStartY = mCurrentY = event.getY();
                if(mSwipeListener != null && !mInThisGesture) {
                    mInThisGesture = mSwipeListener.onOpenSwipe();
                }
                if(mInThisGesture) {
                    if (mSwipeListener != null) {
                        mSwipeListener.onSwipeBegin();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE: {
                mCurrentX = event.getX();
                mCurrentY = event.getY();
                Log.d(TAG, "onTouchEvent: start   X: " + mStartX + "  start Y: " + mStartY);
                Log.d(TAG, "onTouchEvent: current X: " + mCurrentX + "  current Y: " + mCurrentY);
                if(mSwipeListener != null && mInThisGesture) {
                    mSwipeListener.onSwiping(mCurrentX - mStartX, mCurrentY - mStartY);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                mInThisGesture = false;
                if(mSwipeListener != null) {
                     mSwipeListener.onSwipeReleased();
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mInThisGesture = false;
                if(mSwipeListener != null) {
                    mSwipeListener.onSwipeReleased();
                }
                break;
            }
        }
        return mInThisGesture;
    }

    public float getTranslateY() {
        return mCurrentY - mStartY;
    }

    public float getTranslateX() {
        return mCurrentX - mStartX;
    }

    public interface OnSwipeListener {
        boolean onOpenSwipe();
        void onSwipeBegin();
        void onSwipeReleased();
        void onSwiping(float distanceX, float distanceY);
    }
}
