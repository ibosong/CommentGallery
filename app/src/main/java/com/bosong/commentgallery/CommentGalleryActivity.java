package com.bosong.commentgallery;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.bosong.commentgallerylib.CommentGallery;
import com.bosong.commentgallerylib.CommentGalleryContainer;

public class CommentGalleryActivity extends AppCompatActivity {
    private CommentGallery mGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);

        initStatusBar();

        mGallery = (CommentGallery) findViewById(R.id.comment_gallery);
        mGallery.setData((CommentGalleryContainer) getIntent().getSerializableExtra(MainActivity.COMMENT_LIST),
                getIntent().getExtras().getInt(MainActivity.CLICK_INDEX));
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }
}
