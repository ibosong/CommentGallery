package com.bosong.commentgallerylib;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

/**
 * Created by bosong on 2016/12/23.
 */

public class CommentImageGrid extends ViewGroup {
    private static final int IMAGE_BORDER_THICKNESS = 1; //in dp
    private static final int IMAGE_BORDER_COLOR_DEFAULT = 0xffff0000;
    private List<String> mImageUrls;
    private Context mContext;
    @DrawableRes
    int mPlaceholderImageResId;
    @DrawableRes
    int mFailureImageResId;
    private float mHorizontalSpace;
    private float mVerticalSpace;
    private int mImageBorderThickness;
    @ColorInt
    private int mImageBorderColor;
    private int mMaxColumnCount = 3;
    private int mRowCount;
    private int mWidth;
    private OnItemClickListener mOnItemClickListener;
    private int mNewViewCount, mOldViewCount;
    private int mItemWidth;

    public CommentImageGrid(Context context) {
        this(context, null);
    }

    public CommentImageGrid(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentImageGrid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mImageBorderThickness = Utils.dip2px(mContext, IMAGE_BORDER_THICKNESS);
        mImageBorderColor = IMAGE_BORDER_COLOR_DEFAULT;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CommentImageGrid);
        updateStyle(ta);
    }

    public void setData(List<String> imageUrls) {
        if (imageUrls == null) {
            return;
        }

        if (!imageUrls.equals(mImageUrls)) {
            mNewViewCount = imageUrls.size();
            mOldViewCount = mImageUrls != null ? mImageUrls.size() : 0;
            mImageUrls = imageUrls;
            mRowCount = (int) Math.ceil(((double) mNewViewCount) / mMaxColumnCount);

            if (mNewViewCount < mOldViewCount) {
                removeViews(mNewViewCount - 1, mOldViewCount - mNewViewCount);
            } else if (mNewViewCount > mOldViewCount) {
                for (int i = 0; i < mNewViewCount - mOldViewCount; i++) {
                    ViewGroup imageLayout = generateImageLayout();
                    addView(imageLayout);
                }
            } else {
                refreshImageChild();
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mItemWidth = (int) (mWidth - mHorizontalSpace * (mMaxColumnCount - 1)) / mMaxColumnCount;
        int itemHeight = mItemWidth;

        for (int i = 0; i < getChildCount(); ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            int resultMode = MeasureSpec.EXACTLY;
            int resultSize = mItemWidth;
            int childMeasureSpec = MeasureSpec.makeMeasureSpec(resultSize, resultMode);
            child.measure(childMeasureSpec, childMeasureSpec);
        }

        int height = itemHeight * mRowCount + (int) (mVerticalSpace * (mRowCount - 1));

        setMeasuredDimension(mWidth, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        layoutChildrenView();
    }

    public void setPlacholderImageResId(@DrawableRes int placeholderImageResId) {
        mPlaceholderImageResId = placeholderImageResId;
    }

    public void setFallbackImageResId(@DrawableRes int fallbackRes) {
        mFailureImageResId = fallbackRes;
    }

    public void setImageBorderColor(@ColorInt int color) {
        mImageBorderColor = color;
    }

    private void updateStyle(TypedArray styled) {
        mPlaceholderImageResId = styled.getResourceId(R.styleable.CommentImageGrid_placeholder_image, 0);
        mFailureImageResId = styled.getResourceId(R.styleable.CommentImageGrid_fallback_image, 0);
        mHorizontalSpace = styled.getDimension(R.styleable.CommentImageGrid_horizontal_space, 0.f);
        mVerticalSpace = styled.getDimension(R.styleable.CommentImageGrid_vertical_space, 0.f);
        styled.recycle();
    }

    private ViewGroup generateImageLayout() {
        SquareDraweeView imageView = new SquareDraweeView(mContext);

        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(mContext.getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(300).setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
                .build();
        if (mPlaceholderImageResId > 0) {
            hierarchy.setPlaceholderImage(mPlaceholderImageResId);
        }
        if (mFailureImageResId > 0) {
            hierarchy.setFailureImage(mFailureImageResId);
        }

        imageView.setHierarchy(hierarchy);
        RelativeLayout.LayoutParams imgLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imgLp.setMargins(mImageBorderThickness, mImageBorderThickness, mImageBorderThickness, mImageBorderThickness);
        RelativeLayout mainLayout = new RelativeLayout(mContext);
        mainLayout.setBackgroundColor(mImageBorderColor);
        mainLayout.addView(imageView, imgLp);
        return mainLayout;
    }

    private void layoutChildrenView() {
        int childrenCount = getChildCount();

        for (int i = 0; i < childrenCount; i++) {
            ViewGroup childImageLayout = (ViewGroup) getChildAt(i);
            SimpleDraweeView childImageView = (SimpleDraweeView) childImageLayout.getChildAt(0);
            if (mOnItemClickListener != null) {
                final int finalI = i;
                childImageLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.OnItemClick(finalI);
                    }
                });
            }
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(mImageUrls.get(i)))
                    .setProgressiveRenderingEnabled(true)
                    .setResizeOptions(new ResizeOptions(mItemWidth, mItemWidth))
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(childImageView.getController())
                    .build();
            childImageView.setController(controller);

            int[] position = findPosition(i);
            int itemHeight = mItemWidth;
            int left = (int) (mItemWidth + mHorizontalSpace) * position[1];
            int top = (int) (itemHeight + mVerticalSpace) * position[0];
            int right = left + mItemWidth;
            int bottom = top + itemHeight;

            childImageLayout.layout(left, top, right, bottom);
        }
    }

    private void refreshImageChild() {
        int childrenCount = getChildCount();
        if (childrenCount > 0) {
            for (int i = 0; i < childrenCount; i++) {
                ViewGroup childImageLayout = (ViewGroup) getChildAt(i);
                SimpleDraweeView childImageView = (SimpleDraweeView) childImageLayout.getChildAt(0);
                if (mOnItemClickListener != null) {
                    final int finalI = i;
                    childImageLayout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemClickListener.OnItemClick(finalI);
                        }
                    });
                }
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(mImageUrls.get(i)))
                        .setResizeOptions(new ResizeOptions(mItemWidth, mItemWidth))
                        .build();
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(childImageView.getController())
                        .build();
                childImageView.setController(controller);
            }
        }
    }

    private int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < mRowCount; i++) {
            for (int j = 0; j < mMaxColumnCount; j++) {
                if ((i * mMaxColumnCount + j) == childNum) {
                    position[0] = i;//行
                    position[1] = j;//列
                    break;
                }
            }
        }
        return position;
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }
}
