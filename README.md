# CommentGallery

This is an Android sample for demonstrating some common scenarios, such as a grid with images and a large image viewer
which supports zoom-in and zoom-out. Please feel free to let me know if your have any questions.

![](https://github.com/ibosong/CommentGallery/blob/master/CommentGallery.gif)

## Gradle Dependency

Step 1. Add the JitPack repository to your build file:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Step 2. Add the dependency
```
dependencies {
	        compile 'com.github.ibosong:CommentGallery:v1.0'
	}
```

## Usage

1. Usage of LargeImageGallery
 
  - Add LargeImageGallery to your xml file.
  
   ```
    <com.bosong.commentgallerylib.LargeImageGallery
        android:id="@+id/image_gallery"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
   ```
   
    
  - Set image urls with `setData(List<String> urls)` method.
  That's all. There are also some listeners your can set:
  
   ```
   mLargeImageGallery.setOnImageSelectedListener(this);
   mLargeImageGallery.setOnItemClickListener(this);
   ```


2. Usage of CommentImageGrid

    (Editing...)
