package com.bosong.commentgallery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bosong.commentgallerylib.CommentGallery;
import com.bosong.commentgallerylib.CommentGalleryContainer;

public class CommentGalleryActivity extends AppCompatActivity {
    private CommentGallery mGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);

        mGallery = (CommentGallery) findViewById(R.id.comment_gallery);
        mGallery.setData((CommentGalleryContainer) getIntent().getSerializableExtra(MainActivity.COMMENT_LIST),
                getIntent().getExtras().getInt(MainActivity.CLICK_INDEX));
    }
}
