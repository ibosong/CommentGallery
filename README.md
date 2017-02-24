# CommentGallery

This is an Android sample for demonstrating some common scenarios, such as a grid with images and a large image
which support zoom-in and zoom-out. Please feel free to let me know if your have any questions.

![](https://github.com/ibosong/CommentGallery/blob/master/CommentGallery.gif)

## Usage

1. Usage of CommentGallery
 
- Add CommentGallery to your xml file.
```
    <com.bosong.commentgallerylib.CommentGallery
        android:id="@+id/comment_gallery"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintHorizontal_bias="0.0"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.0" />
```
   
    
- Set data with CommentGalleryContainer class, you can also set a default index.

```
     mGallery = (CommentGallery) findViewById(R.id.comment_gallery);
     mGallery.setData((CommentGalleryContainer) getIntent().getSerializableExtra(MainActivity.COMMENT_LIST),
                getIntent().getExtras().getInt(MainActivity.CLICK_INDEX));
```

2. Usage of CommentImageGrid

    (Editing...)
