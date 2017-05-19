package com.bosong.commentgallery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bosong.commentgallerylib.CommentGalleryContainer;
import com.bosong.commentgallerylib.CommentImageGrid;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    public static final String CLICK_INDEX = "CLICK_INDEX";
    public static final String COMMENT_LIST = "COMMENT_LIST";
    private static final String[] SAMPLE_URIS = {
            "https://images-cn.ssl-images-amazon.com/images/I/81ghN3jk6AL._SL1000_.jpg",
            "https://images-cn.ssl-images-amazon.com/images/I/61YK4KgVWLL._SL1000_.jpg",
            "https://images-cn.ssl-images-amazon.com/images/I/81v6YVUdLnL._SL1000_.jpg",
            "https://images-cn.ssl-images-amazon.com/images/I/61y10jAltmL._SL1000_.jpg",
            "https://images-cn.ssl-images-amazon.com/images/I/71owNXqWERL._SL1000_.jpg",
            "https://images-cn.ssl-images-amazon.com/images/G/28/aplus_rbs/iPhone6PC_170223.jpg",
            "https://images-cn.ssl-images-amazon.com/images/G/28/kindle/2016/zhangr/DPfeature_img/voyag_featureimg._CB532421748_.jpg"
    };
    private static final String SAMPLE_COMMENT = "I am somewhat disappointed in the 2015 version as there is not a huge improvement over last year’s model. ";

    private CommentImageGrid mCommentGrid;

    CommentGalleryContainer commentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCommentGrid = (CommentImageGrid) findViewById(R.id.comment_grid);

        List<String> urls = Arrays.asList(SAMPLE_URIS);


        commentList = new CommentGalleryContainer(urls, SAMPLE_COMMENT);

        mCommentGrid.setData(urls);
        mCommentGrid.setOnItemClickListener(new CommentImageGrid.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Intent it = new Intent();
                it.putExtra(CLICK_INDEX, position);
                it.putExtra(COMMENT_LIST, commentList);
                it.setClass(MainActivity.this, CommentGalleryActivity.class);
                startActivity(it);
            }
        });
    }
}
