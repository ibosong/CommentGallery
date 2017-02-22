package com.bosong.commentgallerylib;

import java.io.Serializable;
import java.util.List;

/**
 * Created by boson on 2016/12/21.
 */

public class CommentGalleryContainer implements Serializable {
    private List<String> mImgUrl;
    private String mComment;
    public CommentGalleryContainer(List<String> url, String text){
        mImgUrl = url;
        mComment = text;
    }

    public List<String> getImageUrl(){
        return this.mImgUrl;
    }

    public String getComment(){
        return this.mComment;
    }
}
