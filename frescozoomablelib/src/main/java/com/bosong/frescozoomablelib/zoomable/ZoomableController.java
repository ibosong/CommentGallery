/*
 * This file provided by Facebook is for non-commercial testing and evaluation
 * purposes only.  Facebook reserves all rights not expressly granted.
 *
* Edit to implement these features:
 * 1. Restore scale after releasing fingers when zoomed in or translated in y-axis.
 * 2. Restore to the original size after double tap on the image
 * 3. Display long image
 * 4. Swipe down gesture, generally for closing the gallery.
 *
 * Copyright Facebook, Bo Song
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * FACEBOOK BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.bosong.frescozoomablelib.zoomable;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * Interface for implementing a controller that works with {@link ZoomableDraweeView}
 * to control the zoom.
 */
public interface ZoomableController {

    /**
     * Listener interface.
     */
    interface Listener {

        /**
         * Notifies the view that the transform changed.
         *
         * @param transform the new matrix
         */
        void onTransformChanged(Matrix transform);
    }

    interface OnSwipeDownListener {
        void onSwipeDown(float translateY);
        void onSwipeRelease(float translateY);
    }

    void setSwipeDownListener(OnSwipeDownListener listener);

    /**
     * Enables the controller. The controller is enabled when the image has been loaded.
     *
     * @param enabled whether to enable the controller
     */
    void setEnabled(boolean enabled);

    /**
     * Gets whether the controller is enabled. This should return the last value passed to
     * {@link #setEnabled}.
     *
     * @return whether the controller is enabled.
     */
    boolean isEnabled();

    void setEnableGestureDiscard(boolean discardGesture);

    /**
     * Sets the listener for the controller to call back when the matrix changes.
     *
     * @param listener the listener
     */
    void setListener(Listener listener);

    /**
     * Gets the current scale factor. A convenience method for calculating the scale from the
     * transform.
     *
     * @return the current scale factor
     */
    float getScaleFactor();

    float getOriginScaleFactor();

    float getTranslateY();

    /**
     * Returns true if the zoomable transform is identity matrix, and the controller is idle.
     */
    boolean isIdentity();

    /**
     * Returns true if the transform was corrected during the last update.
     * <p>
     * This mainly happens when a gesture would cause the image to get out of limits and the
     * transform gets corrected in order to prevent that.
     */
    boolean wasTransformCorrected();

    /**
     * See {@link android.support.v4.view.ScrollingView}.
     */
    int computeHorizontalScrollRange();

    int computeHorizontalScrollOffset();

    int computeHorizontalScrollExtent();

    int computeVerticalScrollRange();

    int computeVerticalScrollOffset();

    int computeVerticalScrollExtent();

    /**
     * Gets the current transform.
     *
     * @return the transform
     */
    Matrix getTransform();

    RectF getImageBounds();

    /**
     * Sets the bounds of the image post transform prior to application of the zoomable
     * transformation.
     *
     * @param imageBounds the bounds of the image
     */
    void setImageBounds(RectF imageBounds);

    /**
     * Sets the bounds of the view.
     *
     * @param viewBounds the bounds of the view
     */
    void setViewBounds(RectF viewBounds);

    /**
     * Add by BoSong
     *
     * @param viewBounds
     * @param imageBounds
     */
    void initDefaultScale(RectF viewBounds, RectF imageBounds);

    /**
     * Allows the controller to handle a touch event.
     *
     * @param event the touch event
     * @return whether the controller handled the event
     */
    boolean onTouchEvent(MotionEvent event);
}
