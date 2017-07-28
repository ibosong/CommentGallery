package com.bosong.commentgallery;

import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.bosong.commentgallerylib.CommentGallery;
import com.bosong.commentgallerylib.CommentGalleryContainer;
import com.bosong.frescozoomablelib.zoomable.ZoomableController;

public class CommentGalleryActivity extends AppCompatActivity implements ZoomableController.SwipeDownListener {
    private static final float SWIPE_DOWN_BEGIN_THRESHOLD = 300;

    private CommentGallery mGallery;
    private float mCloseThreshold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);

        mGallery = (CommentGallery) findViewById(R.id.comment_gallery);
        mGallery.setData((CommentGalleryContainer) getIntent().getSerializableExtra(MainActivity.COMMENT_LIST),
                getIntent().getExtras().getInt(MainActivity.CLICK_INDEX));
        mGallery.setSwipeDownListener(this);

        mCloseThreshold = Utils.getWindowHeight(CommentGalleryActivity.this) / 3;
    }

    @Override
    public void onSwipeDown(float y) {
        if(limitTranslateY(y) > SWIPE_DOWN_BEGIN_THRESHOLD) {
            float alpha = SWIPE_DOWN_BEGIN_THRESHOLD /limitTranslateY(y);
            refreshWindowOpacity(alpha);
        }
    }

    @Override
    public void onSwipeDownRelease(float y) {
        if(y > mCloseThreshold) {
            finish();
        } else {
            refreshWindowOpacity(1.0f);
        }
    }

    private void refreshWindowOpacity(@FloatRange(from = 0.0f, to = 1.0f) float alpha) {
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=alpha;
        getWindow().setAttributes(lp);
    }

    private float limitTranslateY(float y) {
        return Math.min(y, Math.max(y, 0));
    }
}
