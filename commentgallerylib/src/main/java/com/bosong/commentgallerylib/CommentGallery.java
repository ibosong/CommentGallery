package com.bosong.commentgallerylib;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by boson on 2016/12/22.
 */

public class CommentGallery extends RelativeLayout implements LargeImageGallery.OnSelectionChangedListener, LargeImageGallery.OnItemClickListener {
    private static final int COLLAPSED_COMMENT_MAX_LINES = 2;
    private static final String INDICATOR_STRING_FORMAT = "%d / %d";

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
        mCommentLayout.setOnClickListener(mOnCommentClickListener);
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


    private void handleCommentVisual(){
        if(mIsCommentCollapsed){
            this.expandComment();
            mIsCommentCollapsed = false;
        }else{
            this.collapseComment();
            mIsCommentCollapsed = true;
        }
    }

    private void expandComment(){
        mTextViewComment.setMaxLines(Integer.MAX_VALUE);
        mTextViewComment.setEllipsize(null);
    }

    private void collapseComment(){
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
}
