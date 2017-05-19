# CommentGallery

This project contains serval librarys for demonstrating image gallery with [Fresco](https://github.com/facebook/fresco)'s [ZoomableDraweeView](https://github.com/facebook/fresco/blob/master/samples/zoomable/src/main/java/com/facebook/samples/zoomable/ZoomableDraweeView.java)(Use the optimized version I made), such as image grid and large image viewer
which supports zoom-in and zoom-out. Please feel free to let me know if your have any questions.

![](https://github.com/ibosong/CommentGallery/blob/master/CommentGallery.gif)

## Gradle Dependency

Add the JitPack repository to your build file:

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
 ```

## Usage

### 1. Usage of ZoomableDraweeView

I've optimized some default behaviors to the official ZoomableDraweeView to make it more perfect.

  - Add the dependency

    ```
    dependencies {
         compile 'com.github.ibosong.CommentGallery:frescozoomablelib:1.0.1'
  	    }
    ```

   - Use `ZoomableDraweeView` as the Fresco `SimpleDraweeView`.

### 2. Usage of LargeImageGallery
 
  - Add the dependency

    ```
    dependencies {
         compile 'com.github.ibosong.CommentGallery:largeimagegallerylib:1.0.1'
  	    }
    ```

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

### 3. Usage of CommentImageGrid

  - Add the dependency

    ```
    dependencies {
         compile 'com.github.ibosong.CommentGallery:commentgallerylib:1.0.1'
  	    }
    ```

  - Add CommentImageGrid to xml layout file

    ```
    <com.bosong.commentgallerylib.CommentImageGrid
        android:id="@+id/comment_grid"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:placeholder_image="@drawable/placeholder"
        app:fallback_image="@drawable/placeholder"
        app:vertical_space="7.5dp"
        app:horizontal_space="7.5dp"/>
    ```

  - Attribures
  
    Attribure| Usage 
    -|-
    placeholder_image | set placeholder image
    fallback_image    | set fallback image
    horizontal_space  | horizontal space between items
    vertical_space    | vertical space between items

    
  - Set image urls with `List<String>`

    ```
    mCommentGrid.setData(urls);
    ```

    ## Attentions

    You may do some compress works for the large image before rendering to avoid the error "OpenGLRenderer: Bitmap too large to be uploaded into a texture".

    ## Last thing

    Enjoy yourself!
