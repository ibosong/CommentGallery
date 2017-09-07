package com.bosong.commentgallerylib;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.FloatRange;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bosong.frescozoomablelib.zoomable.ZoomableController;
import com.bosong.frescozoomablelib.zoomable.ZoomableDraweeView;
import com.bosong.largeimagegallerylib.LargeImageGallery;

import java.util.List;

/**
 * Created by boson on 2016/12/22.
 */

public class CommentGallery extends RelativeLayout implements LargeImageGallery.OnSelectionChangedListener, LargeImageGallery.OnItemClickListener, ZoomableController.OnSwipeDownListener {
    private static final int COLLAPSED_COMMENT_MAX_LINES = 2;
    private static final String INDICATOR_STRING_FORMAT = "%d / %d";
    private static float ALPHA_TRANSLATE_MAX;
    private static float SIZE_TRANSLATE_MAX;
    private static float CLOSE_ACTIVITY_THRESHOLD;

    private Context mContext;
    private RelativeLayout mTitleLayout;
    private RelativeLayout mCommentLayout;
    private LargeImageGallery mLargeImageGallery;
    private TextView mTextViewComment;
    private TextView mTextViewIndicator;

    private CommentGalleryContainer mCommentData;

    private boolean mIsCommentCollapsed = true;

    private OnClickListener mOnCommentClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            handleCommentVisual();
        }
    };

    public CommentGallery(Context context) {
        this(context, null);
    }

    public CommentGallery(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentGallery(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ALPHA_TRANSLATE_MAX = Utils.getWindowHeight(context) / 3;
        SIZE_TRANSLATE_MAX = Utils.getWindowHeight(context) / 2;
        CLOSE_ACTIVITY_THRESHOLD = Utils.getWindowHeight(context) / 3;
        mContext = context;
        initView();
    }

    public void setData(CommentGalleryContainer data){
        mCommentData = data;
        initViewData();
    }

    public void setData(CommentGalleryContainer data, int currentItem){
        setData(data);
        mLargeImageGallery.setCurrentItem(currentItem);
    }

    private void initView(){
        LayoutInflater.from(mContext).inflate(R.layout.comment_gallery, this);
        mLargeImageGallery = (LargeImageGallery) findViewById(R.id.image_gallery);
        mTitleLayout = (RelativeLayout) findViewById(R.id.rl_title_layout);
        mCommentLayout = (RelativeLayout) findViewById(R.id.rl_comment_layout);
        mTextViewComment = (TextView) findViewById(R.id.tv_comment);
        mTextViewIndicator = (TextView) findViewById(R.id.tv_indicator);

        mLargeImageGallery.setOnImageSelectedListener(this);
        mLargeImageGallery.setOnItemClickListener(this);
        mLargeImageGallery.setOnSwipeDownListener(this);
        mCommentLayout.setOnClickListener(mOnCommentClickListener);
        setBackgroundAlpha(this, 1.0f);
    }

    private void initViewData(){
        if(mCommentData != null){
            mTextViewComment.setText(mCommentData.getComment());
            List<String> urls = getUrls();
            if(urls != null && urls.size() > 0){
                mLargeImageGallery.setData(urls);
                mTextViewIndicator.setText(String.format(INDICATOR_STRING_FORMAT, 1, urls.size()));
            }
        }
    }

    private List<String> getUrls(){
        List<String> urls = null;
        if(mCommentData != null){
            urls = mCommentData.getImageUrl();
        }
        return urls;
    }

    private void handleMaskVisual(){
        if(mTitleLayout.getVisibility() == VISIBLE){
            mTitleLayout.setVisibility(GONE);
            mCommentLayout.setVisibility(GONE);
        }else{
            mTitleLayout.setVisibility(VISIBLE);
            mCommentLayout.setVisibility(VISIBLE);
        }
    }

    private void handleCommentVisual() {
        if(mIsCommentCollapsed){
            this.expandComment();
            mIsCommentCollapsed = false;
        }else{
            this.collapseComment();
            mIsCommentCollapsed = true;
        }
    }

    private void expandComment() {
        mTextViewComment.setMaxLines(Integer.MAX_VALUE);
        mTextViewComment.setEllipsize(null);
    }

    private void collapseComment() {
        mTextViewComment.setMaxLines(COLLAPSED_COMMENT_MAX_LINES);
        mTextViewComment.setEllipsize(TextUtils.TruncateAt.END);
    }

    @Override
    public void onSelectionChanged(int lastIndex, int currentIndex) {
        mTextViewIndicator.setText(String.format(INDICATOR_STRING_FORMAT, currentIndex + 1, mCommentData.getImageUrl().size()));
    }

    @Override
    public void onItemClick(int currentIndex) {
        handleMaskVisual();
    }

    @Override
    public void onSwipeDown(float translateY) {
        float alpha = limitTranslateY((ALPHA_TRANSLATE_MAX - translateY)/ALPHA_TRANSLATE_MAX);
        setBackgroundAlpha(this, alpha);

        if(mTitleLayout.getVisibility() == VISIBLE){
            mTitleLayout.setVisibility(GONE);
            mCommentLayout.setVisibility(GONE);
        }
    }

    @Override
    public void onSwipeRelease(float translateY) {
        if(translateY >= CLOSE_ACTIVITY_THRESHOLD) { // need close activity
            ZoomableDraweeView currentItemView = mLargeImageGallery.getCurrentItemView();
            if(currentItemView != null) {
                currentItemView.setEnableGestureDiscard(false);
                TranslateAnimation animation = new TranslateAnimation(0.f, 0.f, currentItemView.getZoomableController().getImageBounds().top, Utils.getWindowHeight(mContext));
                animation.setDuration(200);
                animation.setAnimationListener(new Animation.AnimationListener()  {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if(mContext instanceof Activity) {
                            refreshWindowOpacity(0.0f);
                            ((Activity)mContext).finish();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                startAnimation(animation);
            }

        } else {
            setBackgroundAlpha(this, 1.0f);
            if(mTitleLayout.getVisibility() == GONE){
                mTitleLayout.setVisibility(VISIBLE);
                mCommentLayout.setVisibility(VISIBLE);
            }
        }
    }

    private float limitTranslateY(float y) {
        return Math.min(1, Math.max(y, 0));
    }

    private void setBackgroundAlpha(View view, @FloatRange(from = 0.f, to = 1.f) float alpha) {
        int xxx = ((byte)(0xff * alpha) << 24);
        view.setBackgroundColor(xxx);
    }

    private void refreshWindowOpacity(@FloatRange(from = 0.0f, to = 1.0f) float alpha) {
        if(!(mContext instanceof Activity)) {
            return;
        }
        Window window = ((Activity)mContext).getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha=alpha;
        window.setAttributes(lp);
    }
}
